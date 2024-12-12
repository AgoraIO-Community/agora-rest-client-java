package io.agora.rest.core;

import io.agora.rest.exception.AgoraUnknownException;
import io.netty.handler.codec.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.netty.ChannelBindException;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import java.net.UnknownHostException;
import java.time.Duration;

public class DefaultContext implements Context {

    private static final Logger logger = LoggerFactory.getLogger(DefaultContext.class);

    private final AgoraConfig agoraConfig;

    private final HttpClient httpClient;

    private final Codec codec;

    private final DomainPool domainPool;

    public DefaultContext(AgoraConfig agoraConfig) {
        this.agoraConfig = agoraConfig;
        this.httpClient = HttpClientFactory.createHttpClient(agoraConfig);
        this.domainPool = new DomainPool(agoraConfig.getRegionArea());
        this.codec = new JsonCodec();
    }

    @Override
    public <T> Mono<T> sendRequest(String path, HttpMethod method, Object requestBody, Class<T> clazz) {
        return Mono.defer(() -> {
            this.domainPool.selectBestDomain();

            return Mono.defer(() -> httpClient
                            .doOnRequest((req, conn) ->
                                    logger.debug("request:{},{}{}", req.method(), req.requestHeaders().get("host"), req.uri()))
                            .headers(h -> h.add("Content-Type", "application/json"))
                            .request(method)
                            .uri(Mono.just(this.domainPool.getCurrentUrl() + path))
                            .send(Mono.create(sink -> sink.success(codec.encode(requestBody))))
                            .responseSingle((rsp, buf) -> buf.switchIfEmpty(
                                            Mono.error(new AgoraUnknownException("Empty response from Agora server")))
                                    .flatMap(byteBuf -> {
                                        ErrorMapper mapper = new DefaultErrorMapper();

                                        mapper.statusCode(rsp);
                                        mapper.checkError(byteBuf);

                                        return Mono.just(byteBuf);
                                    })
                            ).map(buf -> {
                                T response = codec.decode(buf, clazz);
                                buf.release();
                                logger.debug("response:{}", response);
                                return response;
                            }))
                    .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(500)).
                            filter(e -> e instanceof ChannelBindException || e instanceof UnknownHostException)
                            .doBeforeRetry(retrySignal -> {
                                domainPool.nextRegion();
                                logger.warn("Retry attempt: {}, next backoff: {}", retrySignal.totalRetries() + 1, Duration.ofMillis(500));
                            })
                            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                                logger.error("Retry exhausted: {}", retrySignal.totalRetries());
                                return retrySignal.failure();
                            }));
        });
    }

    @Override
    public AgoraConfig getAgoraConfig() {
        return this.agoraConfig;
    }
}
