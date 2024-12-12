package io.agora.rest.services.cloudrecording.api;

import io.agora.rest.core.Context;
import io.agora.rest.services.cloudrecording.api.req.UpdateLayoutResourceReq;
import io.agora.rest.services.cloudrecording.api.req.UpdateResourceReq;
import io.agora.rest.services.cloudrecording.api.res.UpdateLayoutResourceRes;
import io.agora.rest.services.cloudrecording.api.res.UpdateResourceRes;
import io.agora.rest.services.cloudrecording.enums.CloudRecordingModeEnum;
import io.netty.handler.codec.http.HttpMethod;
import reactor.core.publisher.Mono;

public class UpdateResourceAPI {

    private final Context context;

    public UpdateResourceAPI(Context context) {
        this.context = context;
    }

    public Mono<UpdateResourceRes> handle(String resourceId, String sid, CloudRecordingModeEnum mode,
                                          UpdateResourceReq request) {
        String path = String.format("/v1/apps/%s/cloud_recording/resourceid/%s/sid/%s/mode/%s/update",
                this.context.getAgoraConfig().getAppId(),
                resourceId,
                sid,
                mode.getMode());
        return this.context.sendRequest(path, HttpMethod.POST, request, UpdateResourceRes.class);
    }

    public Mono<UpdateLayoutResourceRes> handleLayout(String resourceId, String sid, CloudRecordingModeEnum mode,
                                                      UpdateLayoutResourceReq request) {
        String path = String.format("/v1/apps/%s/cloud_recording/resourceid/%s/sid/%s/mode/%s/updateLayout",
                this.context.getAgoraConfig().getAppId(),
                resourceId,
                sid,
                mode.getMode());
        return this.context.sendRequest(path, HttpMethod.POST, request, UpdateLayoutResourceRes.class);
    }
}
