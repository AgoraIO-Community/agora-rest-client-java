package io.agora.rest.services.cloudrecording;

import io.agora.rest.core.AgoraConfig;
import io.agora.rest.core.Context;
import io.agora.rest.core.DefaultContext;
import io.agora.rest.services.cloudrecording.api.*;
import io.agora.rest.services.cloudrecording.api.req.*;
import io.agora.rest.services.cloudrecording.api.res.*;
import io.agora.rest.services.cloudrecording.enums.CloudRecordingModeEnum;
import io.agora.rest.services.cloudrecording.scenario.individual.IndividualScenario;
import io.agora.rest.services.cloudrecording.scenario.mix.MixScenario;
import io.agora.rest.services.cloudrecording.scenario.web.WebScenario;
import reactor.core.publisher.Mono;

public class CloudRecordingService {

    private final AcquireResourceAPI acquireResourceAPI;

    private final QueryResourceAPI queryResourceAPI;

    private final StartResourceAPI startResourceAPI;

    private final UpdateResourceAPI updateResourceAPI;

    private final StopResourceAPI stopResourceAPI;

    private final IndividualScenario individualScenario;

    private final WebScenario webScenario;

    private final MixScenario mixScenario;

    public static CloudRecordingService create(AgoraConfig agoraConfig) {
        return new CloudRecordingService(new DefaultContext(agoraConfig));
    }

    protected CloudRecordingService(Context context) {
        this.acquireResourceAPI = new AcquireResourceAPI(context);
        this.queryResourceAPI = new QueryResourceAPI(context);
        this.startResourceAPI = new StartResourceAPI(context);
        this.stopResourceAPI = new StopResourceAPI(context);
        this.updateResourceAPI = new UpdateResourceAPI(context);

        this.individualScenario = new IndividualScenario(acquireResourceAPI, queryResourceAPI, startResourceAPI,
                updateResourceAPI, stopResourceAPI);
        this.webScenario = new WebScenario(acquireResourceAPI, queryResourceAPI, startResourceAPI, updateResourceAPI,
                stopResourceAPI);
        this.mixScenario = new MixScenario(acquireResourceAPI, queryResourceAPI, startResourceAPI, updateResourceAPI,
                stopResourceAPI);
    }

    public Mono<AcquireResourceRes> acquire(AcquireResourceReq request) {
        return acquireResourceAPI.handle(request);
    }

    public Mono<StartResourceRes> start(String resourceId, CloudRecordingModeEnum mode, StartResourceReq request) {
        return startResourceAPI.handle(resourceId, mode, request);
    }

    public Mono<QueryResourceRes> query(String resourceId, String sid, CloudRecordingModeEnum mode) {
        return queryResourceAPI.handle(resourceId, sid, mode);
    }

    public Mono<StopResourceRes> stop(String resourceId, String sid, CloudRecordingModeEnum mode,
                                      StopResourceReq request) {
        return stopResourceAPI.handle(resourceId, sid, mode, request);
    }

    public Mono<UpdateResourceRes> update(String resourceId, String sid, CloudRecordingModeEnum mode,
                                          UpdateResourceReq request) {
        return updateResourceAPI.handle(resourceId, sid, mode, request);
    }

    public Mono<UpdateLayoutResourceRes> updateLayout(String resourceId, String sid, CloudRecordingModeEnum mode,
                                                      UpdateLayoutResourceReq request) {
        return updateResourceAPI.handleLayout(resourceId, sid, mode, request);
    }

    public IndividualScenario individualScenario() {
        return individualScenario;
    }

    public WebScenario webScenario() {
        return webScenario;
    }

    public MixScenario mixScenario() {
        return mixScenario;
    }
}
