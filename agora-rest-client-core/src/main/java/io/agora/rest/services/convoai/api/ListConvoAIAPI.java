package io.agora.rest.services.convoai.api;

import io.agora.rest.core.Context;
import io.agora.rest.exception.AgoraNeedRetryException;
import io.agora.rest.services.convoai.req.ListConvoAIReq;
import io.agora.rest.services.convoai.res.ListConvoAIRes;
import io.netty.handler.codec.http.HttpMethod;
import reactor.core.publisher.Mono;

public class ListConvoAIAPI extends BaseAPI {

    public ListConvoAIAPI(Context context, String pathPrefix, Integer maxAttempts) {
        super(context, pathPrefix, maxAttempts);
    }

    public Mono<ListConvoAIRes> handle(ListConvoAIReq request) {
        StringBuilder path = new StringBuilder(String.format("%s/agents",
                pathPrefix));

        if (request != null && request.toQueryString() != null) {
            path.append(String.format("?%s", request.toQueryString()));
        }

        logger.debug("path:{}", path);

        return this.context.sendRequest(path.toString(), HttpMethod.GET, null, ListConvoAIRes.class)
                .retryWhen(customRetry(e -> e instanceof AgoraNeedRetryException));
    }
}
