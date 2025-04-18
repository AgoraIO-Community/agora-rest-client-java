package io.agora.rest.services.convoai.api;

import io.agora.rest.core.Context;
import io.agora.rest.services.convoai.req.SpeakConvoAIReq;
import io.agora.rest.services.convoai.res.SpeakConvoAIRes;
import io.netty.handler.codec.http.HttpMethod;
import reactor.core.publisher.Mono;

public class SpeakConvoAIAPI {
    private final Context context;

    private final String pathPrefix;

    public SpeakConvoAIAPI(Context context, String pathPrefix) {
        this.context = context;
        this.pathPrefix = pathPrefix;
    }

    public Mono<SpeakConvoAIRes> handle(String agentId, SpeakConvoAIReq request) {
        String path = String.format("%s/agents/%s/speak", pathPrefix, agentId);
        return this.context.sendRequest(path, HttpMethod.POST, request, SpeakConvoAIRes.class);
    }

}
