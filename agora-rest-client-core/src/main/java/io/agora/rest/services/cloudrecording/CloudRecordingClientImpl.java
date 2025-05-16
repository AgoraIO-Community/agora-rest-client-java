package io.agora.rest.services.cloudrecording;

import io.agora.rest.core.Context;
import io.agora.rest.services.cloudrecording.api.*;
import io.agora.rest.services.cloudrecording.api.req.*;
import io.agora.rest.services.cloudrecording.api.res.*;
import io.agora.rest.services.cloudrecording.enums.CloudRecordingModeEnum;
import io.agora.rest.services.cloudrecording.scenario.individual.IndividualScenario;
import io.agora.rest.services.cloudrecording.scenario.individual.IndividualScenarioImpl;
import io.agora.rest.services.cloudrecording.scenario.mix.MixScenario;
import io.agora.rest.services.cloudrecording.scenario.mix.MixScenarioImpl;
import io.agora.rest.services.cloudrecording.scenario.web.WebScenario;
import io.agora.rest.services.cloudrecording.scenario.web.WebScenarioImpl;
import reactor.core.publisher.Mono;

public class CloudRecordingClientImpl extends CloudRecordingClient {

    private final AcquireResourceAPI acquireResourceAPI;

    private final QueryResourceAPI queryResourceAPI;

    private final StartResourceAPI startResourceAPI;

    private final UpdateResourceAPI updateResourceAPI;

    private final StopResourceAPI stopResourceAPI;

    private final IndividualScenario individualScenario;

    private final WebScenario webScenario;

    private final MixScenario mixScenario;

    private final static Integer MAX_ATTEMPTS = 3;

    private final static String pathPrefix = "/v1/apps/%s/cloud_recording";

    protected CloudRecordingClientImpl(Context context) {
        this.acquireResourceAPI = new AcquireResourceAPI(context, pathPrefix, MAX_ATTEMPTS);
        this.queryResourceAPI = new QueryResourceAPI(context, pathPrefix, MAX_ATTEMPTS);
        this.startResourceAPI = new StartResourceAPI(context, pathPrefix, MAX_ATTEMPTS);
        this.stopResourceAPI = new StopResourceAPI(context, pathPrefix, MAX_ATTEMPTS);
        this.updateResourceAPI = new UpdateResourceAPI(context, pathPrefix, MAX_ATTEMPTS);

        this.individualScenario = new IndividualScenarioImpl(acquireResourceAPI, queryResourceAPI, startResourceAPI,
                updateResourceAPI, stopResourceAPI);
        this.webScenario = new WebScenarioImpl(acquireResourceAPI, queryResourceAPI, startResourceAPI,
                updateResourceAPI,
                stopResourceAPI);
        this.mixScenario = new MixScenarioImpl(acquireResourceAPI, queryResourceAPI, startResourceAPI,
                updateResourceAPI,
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
