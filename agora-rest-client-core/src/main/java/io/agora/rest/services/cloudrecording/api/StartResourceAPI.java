package io.agora.rest.services.cloudrecording.api;

import io.agora.rest.core.Context;
import io.agora.rest.exception.AgoraNeedRetryException;
import io.agora.rest.services.cloudrecording.api.req.StartResourceReq;
import io.agora.rest.services.cloudrecording.api.res.StartResourceRes;
import io.agora.rest.services.cloudrecording.enums.CloudRecordingModeEnum;
import io.netty.handler.codec.http.HttpMethod;
import reactor.core.publisher.Mono;

public class StartResourceAPI extends BaseAPI {

    public StartResourceAPI(Context context, String pathPrefix, Integer maxAttempts) {
        super(context, pathPrefix, maxAttempts);
    }

    public Mono<StartResourceRes> handle(String resourceId, CloudRecordingModeEnum mode, StartResourceReq request) {
        String path = String.format("%s/resourceid/%s/mode/%s/start",
                this.pathPrefix, resourceId, mode.getMode());

        return this.context.sendRequest(path, HttpMethod.POST, request, StartResourceRes.class)
                .retryWhen(customRetry(e -> e instanceof AgoraNeedRetryException));
    }

}
