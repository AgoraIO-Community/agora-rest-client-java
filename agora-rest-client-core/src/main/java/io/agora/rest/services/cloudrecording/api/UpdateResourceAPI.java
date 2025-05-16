package io.agora.rest.services.cloudrecording.api;

import io.agora.rest.core.Context;
import io.agora.rest.exception.AgoraNeedRetryException;
import io.agora.rest.services.cloudrecording.api.req.UpdateLayoutResourceReq;
import io.agora.rest.services.cloudrecording.api.req.UpdateResourceReq;
import io.agora.rest.services.cloudrecording.api.res.UpdateLayoutResourceRes;
import io.agora.rest.services.cloudrecording.api.res.UpdateResourceRes;
import io.agora.rest.services.cloudrecording.enums.CloudRecordingModeEnum;
import io.netty.handler.codec.http.HttpMethod;
import reactor.core.publisher.Mono;

public class UpdateResourceAPI extends BaseAPI {

    public UpdateResourceAPI(Context context, String pathPrefix, Integer maxAttempts) {
        super(context, pathPrefix, maxAttempts);
    }

    public Mono<UpdateResourceRes> handle(String resourceId, String sid, CloudRecordingModeEnum mode,
            UpdateResourceReq request) {
        String path = String.format("%s/resourceid/%s/sid/%s/mode/%s/update",
                this.pathPrefix,
                resourceId,
                sid,
                mode.getMode());
        return this.context.sendRequest(path, HttpMethod.POST, request, UpdateResourceRes.class)
                .retryWhen(customRetry(e -> e instanceof AgoraNeedRetryException));
    }

    public Mono<UpdateLayoutResourceRes> handleLayout(String resourceId, String sid, CloudRecordingModeEnum mode,
            UpdateLayoutResourceReq request) {
        String path = String.format("%s/resourceid/%s/sid/%s/mode/%s/updateLayout",
                this.pathPrefix,
                resourceId,
                sid,
                mode.getMode());
        return this.context.sendRequest(path, HttpMethod.POST, request, UpdateLayoutResourceRes.class)
                .retryWhen(customRetry(e -> e instanceof AgoraNeedRetryException));
    }
}
