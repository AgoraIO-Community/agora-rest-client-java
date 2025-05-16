package io.agora.rest.services.convoai.api;

import io.agora.rest.core.Context;
import io.agora.rest.exception.AgoraNeedRetryException;
import io.agora.rest.services.convoai.res.QueryConvoAIRes;
import io.netty.handler.codec.http.HttpMethod;
import reactor.core.publisher.Mono;

public class QueryConvoAIAPI extends BaseAPI {

    public QueryConvoAIAPI(Context context, String pathPrefix, Integer maxAttempts) {
        super(context, pathPrefix, maxAttempts);
    }

    public Mono<QueryConvoAIRes> handle(String agentId) {
        String path = String.format("%s/agents/%s", pathPrefix, agentId);
        return this.context.sendRequest(path, HttpMethod.GET, null, QueryConvoAIRes.class)
                .retryWhen(customRetry(e -> e instanceof AgoraNeedRetryException));
    }
}
