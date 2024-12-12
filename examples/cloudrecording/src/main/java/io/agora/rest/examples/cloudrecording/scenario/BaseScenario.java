package io.agora.rest.examples.cloudrecording.scenario;

import io.agora.rest.core.AgoraConfig;
import io.agora.rest.core.Credential;
import io.agora.rest.core.RegionArea;
import io.agora.rest.services.cloudrecording.CloudRecordingClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseScenario {

    private static final Logger logger = LoggerFactory.getLogger(BaseScenario.class);

    protected final RegionArea region;

    protected final String appId;

    protected final String cname;

    protected final String uid;

    protected final Credential credential;

    protected final CloudRecordingClient cloudRecordingClient;

    public BaseScenario(RegionArea region, String appId, String cname, String uid, Credential credential) {
        this.region = region;
        this.appId = appId;
        this.cname = cname;
        this.uid = uid;
        this.credential = credential;

        AgoraConfig agoraConfig = AgoraConfig.builder()
                .appId(appId)
                .credential(credential)
                .regionArea(region)
                .build();

        logger.info("AgoraConfig: {}", agoraConfig);

        this.cloudRecordingClient = CloudRecordingClient.create(agoraConfig);
    }
}
