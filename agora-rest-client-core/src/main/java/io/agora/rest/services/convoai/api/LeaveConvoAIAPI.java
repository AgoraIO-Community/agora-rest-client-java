package io.agora.rest.services.convoai.api;

import io.agora.rest.core.Context;
import io.agora.rest.exception.AgoraNeedRetryException;
import io.netty.handler.codec.http.HttpMethod;
import reactor.core.publisher.Mono;

public class LeaveConvoAIAPI extends BaseAPI {

    public LeaveConvoAIAPI(Context context, String pathPrefix, Integer maxAttempts) {
        super(context, pathPrefix, maxAttempts);
    }

    public Mono<Void> handle(String agentId) {
        String path = String.format("%s/agents/%s/leave", pathPrefix, agentId);
        return this.context.sendRequest(path, HttpMethod.POST, null, Void.class)
                .retryWhen(customRetry(e -> e instanceof AgoraNeedRetryException));
    }
}
