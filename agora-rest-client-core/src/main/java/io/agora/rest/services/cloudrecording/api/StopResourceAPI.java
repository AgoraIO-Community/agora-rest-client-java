package io.agora.rest.services.cloudrecording.api;

import io.agora.rest.core.Context;
import io.agora.rest.exception.AgoraNeedRetryException;
import io.agora.rest.services.cloudrecording.api.req.StopResourceReq;
import io.agora.rest.services.cloudrecording.api.res.StopResourceRes;
import io.agora.rest.services.cloudrecording.enums.CloudRecordingModeEnum;
import io.netty.handler.codec.http.HttpMethod;
import reactor.core.publisher.Mono;

public class StopResourceAPI extends BaseAPI {

    public StopResourceAPI(Context context, String pathPrefix, Integer maxAttempts) {
        super(context, pathPrefix, maxAttempts);
    }

    public Mono<StopResourceRes> handle(String resourceId, String sid, CloudRecordingModeEnum mode,
            StopResourceReq request) {
        String path = String.format("%s/resourceid/%s/sid/%s/mode/%s/stop",
                this.pathPrefix, resourceId, sid, mode.getMode());
        return this.context.sendRequest(path, HttpMethod.POST, request, StopResourceRes.class)
                .retryWhen(customRetry(e -> e instanceof AgoraNeedRetryException));
    }
}
