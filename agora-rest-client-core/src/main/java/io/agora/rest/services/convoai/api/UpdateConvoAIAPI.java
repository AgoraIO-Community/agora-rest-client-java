package io.agora.rest.services.convoai.api;

import io.agora.rest.core.Context;
import io.agora.rest.exception.AgoraNeedRetryException;
import io.agora.rest.services.convoai.req.UpdateConvoAIReq;
import io.agora.rest.services.convoai.res.UpdateConvoAIRes;
import io.netty.handler.codec.http.HttpMethod;
import reactor.core.publisher.Mono;

public class UpdateConvoAIAPI extends BaseAPI {

    public UpdateConvoAIAPI(Context context, String pathPrefix, Integer maxAttempts) {
        super(context, pathPrefix, maxAttempts);
    }

    public Mono<UpdateConvoAIRes> handle(String agentId, UpdateConvoAIReq request) {
        String path = String.format("%s/agents/%s/update", pathPrefix, agentId);
        return this.context.sendRequest(path, HttpMethod.POST, request, UpdateConvoAIRes.class)
                .retryWhen(customRetry(e -> e instanceof AgoraNeedRetryException));
    }
}
