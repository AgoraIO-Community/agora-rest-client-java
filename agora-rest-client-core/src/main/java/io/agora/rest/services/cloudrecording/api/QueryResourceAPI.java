package io.agora.rest.services.cloudrecording.api;

import io.agora.rest.core.Context;
import io.agora.rest.exception.AgoraJsonException;
import io.agora.rest.services.cloudrecording.api.res.QueryResourceRes;
import io.agora.rest.services.cloudrecording.enums.CloudRecordingModeEnum;
import io.netty.handler.codec.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class QueryResourceAPI {

    private static final Logger logger = LoggerFactory.getLogger(QueryResourceAPI.class);

    private final Context context;

    public QueryResourceAPI(Context context) {
        this.context = context;
    }

    public Mono<QueryResourceRes> handle(String resourceId, String sid, CloudRecordingModeEnum mode) {
        String path = String.format("/v1/apps/%s/cloud_recording/resourceid/%s/sid/%s/mode/%s/query",
                this.context.getAgoraConfig().getAppId(), resourceId, sid, mode.getMode());
        return this.context.sendRequest(path, HttpMethod.GET, null, QueryResourceRes.class)
                .handle((resp, sink) -> {
                    try {
                        resp.setServerResponse(mode);
                    } catch (Exception e) {
                        sink.error(new AgoraJsonException("Failed to set server response", e));
                        return;
                    }

                    logger.debug("response:{}", resp);
                    sink.next(resp);
                });
    }
}
