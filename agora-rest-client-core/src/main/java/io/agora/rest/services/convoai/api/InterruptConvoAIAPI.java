package io.agora.rest.services.convoai.api;

import java.util.HashMap;
import java.util.Map;

import io.agora.rest.core.Context;
import io.agora.rest.exception.AgoraNeedRetryException;
import io.agora.rest.services.convoai.res.InterruptConvoAIRes;
import io.netty.handler.codec.http.HttpMethod;
import reactor.core.publisher.Mono;

public class InterruptConvoAIAPI extends BaseAPI {

    public InterruptConvoAIAPI(Context context, String pathPrefix, Integer maxAttempts) {
        super(context, pathPrefix, maxAttempts);
    }

    public Mono<InterruptConvoAIRes> handle(String agentId) {
        String path = String.format("%s/agents/%s/interrupt", pathPrefix, agentId);
        // use empty map as body
        Map<String, Object> body = new HashMap<>();
        return this.context.sendRequest(path, HttpMethod.POST, body, InterruptConvoAIRes.class)
                .retryWhen(customRetry(e -> e instanceof AgoraNeedRetryException));
    }
}
