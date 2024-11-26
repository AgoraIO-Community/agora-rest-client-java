package io.agora.rest.services.cloudrecording.api;

import io.agora.rest.core.Context;
import io.agora.rest.services.cloudrecording.api.req.StopResourceReq;
import io.agora.rest.services.cloudrecording.api.res.StopResourceRes;
import io.agora.rest.services.cloudrecording.enums.CloudRecordingModeEnum;
import io.netty.handler.codec.http.HttpMethod;
import reactor.core.publisher.Mono;

public class StopResourceAPI {

    private final Context context;

    public StopResourceAPI(Context context) {
        this.context = context;
    }

    public Mono<StopResourceRes> handle(String resourceId, String sid, CloudRecordingModeEnum mode,
                                        StopResourceReq request) {
        String path = String.format("/v1/apps/%s/cloud_recording/resourceid/%s/sid/%s/mode/%s/stop",
                this.context.getAgoraConfig().getAppId(), resourceId, sid, mode.getMode());
        return this.context.sendRequest(path, HttpMethod.POST, request, StopResourceRes.class);
    }
}
