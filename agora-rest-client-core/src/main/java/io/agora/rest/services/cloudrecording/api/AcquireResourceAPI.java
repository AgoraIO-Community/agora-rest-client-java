package io.agora.rest.services.cloudrecording.api;

import io.agora.rest.core.Context;
import io.agora.rest.services.cloudrecording.api.req.AcquireResourceReq;
import io.agora.rest.services.cloudrecording.api.res.AcquireResourceRes;
import io.netty.handler.codec.http.HttpMethod;
import reactor.core.publisher.Mono;

public class AcquireResourceAPI {

    private final Context context;

    public AcquireResourceAPI(Context context) {
        this.context = context;
    }

    public Mono<AcquireResourceRes> handle(AcquireResourceReq request) {
        String path = String.format("/v1/apps/%s/cloud_recording/acquire",
                this.context.getAgoraConfig().getAppId());
        return this.context.sendRequest(path, HttpMethod.POST, request, AcquireResourceRes.class);
    }
}
