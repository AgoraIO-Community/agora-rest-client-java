package io.agora.rest.services.cloudrecording.api;

import io.agora.rest.core.Context;
import io.agora.rest.exception.AgoraNeedRetryException;
import io.agora.rest.services.cloudrecording.api.req.AcquireResourceReq;
import io.agora.rest.services.cloudrecording.api.res.AcquireResourceRes;
import io.netty.handler.codec.http.HttpMethod;
import reactor.core.publisher.Mono;

public class AcquireResourceAPI extends BaseAPI {

    public AcquireResourceAPI(Context context, String pathPrefix, Integer maxAttempts) {
        super(context, pathPrefix, maxAttempts);
    }

    public Mono<AcquireResourceRes> handle(AcquireResourceReq request) {
        String path = String.format("%s/acquire",
                this.pathPrefix);
        return this.context.sendRequest(path, HttpMethod.POST, request, AcquireResourceRes.class)
                .retryWhen(customRetry(e -> e instanceof AgoraNeedRetryException));
    }
}
