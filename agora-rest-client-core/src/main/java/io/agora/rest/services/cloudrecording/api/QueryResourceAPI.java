package io.agora.rest.services.cloudrecording.api;

import io.agora.rest.core.Context;
import io.agora.rest.exception.AgoraJsonException;
import io.agora.rest.exception.AgoraNeedRetryException;
import io.agora.rest.services.cloudrecording.api.res.QueryResourceRes;
import io.agora.rest.services.cloudrecording.enums.CloudRecordingModeEnum;
import io.netty.handler.codec.http.HttpMethod;
import reactor.core.publisher.Mono;

public class QueryResourceAPI extends BaseAPI {

    public QueryResourceAPI(Context context, String pathPrefix, Integer maxAttempts) {
        super(context, pathPrefix, maxAttempts);
    }

    public Mono<QueryResourceRes> handle(String resourceId, String sid, CloudRecordingModeEnum mode) {
        String path = String.format("%s/resourceid/%s/sid/%s/mode/%s/query",
                this.pathPrefix, resourceId, sid, mode.getMode());
        return this.context.sendRequest(path, HttpMethod.GET, null, QueryResourceRes.class)
                .retryWhen(customRetry(e -> e instanceof AgoraNeedRetryException))
                .handle((resp, sink) -> {
                    try {
                        resp.setServerResponse(mode);
                    } catch (Exception e) {
                        sink.error(new AgoraJsonException("Failed to set server response", e));
                        return;
                    }

                    logger.debug("response:{}", resp);
                    sink.next(resp);
                });
    }
}
