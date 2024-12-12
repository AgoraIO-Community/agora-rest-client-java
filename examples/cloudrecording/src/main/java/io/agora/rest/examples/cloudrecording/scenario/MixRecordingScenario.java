package io.agora.rest.examples.cloudrecording.scenario;

import io.agora.rest.AgoraException;
import io.agora.rest.core.Credential;
import io.agora.rest.core.RegionArea;
import io.agora.rest.services.cloudrecording.api.req.StartResourceReq;
import io.agora.rest.services.cloudrecording.api.req.UpdateResourceReq;
import io.agora.rest.services.cloudrecording.api.res.*;
import io.agora.rest.services.cloudrecording.scenario.mix.req.AcquireMixRecordingResourceClientReq;
import io.agora.rest.services.cloudrecording.scenario.mix.req.StartMixRecordingResourceClientReq;
import io.agora.rest.services.cloudrecording.scenario.mix.req.UpdateLayoutMixRecordingResourceClientReq;
import io.agora.rest.services.cloudrecording.scenario.mix.req.UpdateMixRecordingResourceClientReq;
import io.agora.rest.services.cloudrecording.scenario.mix.res.QueryMixHLSAndMP4RecordingResourceRes;
import io.agora.rest.services.cloudrecording.scenario.mix.res.QueryMixHLSRecordingResourceRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;

public class MixRecordingScenario extends BaseScenario {
    private static final Logger logger = LoggerFactory.getLogger(MixRecordingScenario.class);

    public MixRecordingScenario(RegionArea region, String appId, String cname, String uid, Credential credential) {
        super(region, appId, cname, uid, credential);
    }

    public void runHLS(String token, StartResourceReq.StorageConfig storageConfig) {
        // Run HLS implementation

        // acquire
        AcquireResourceRes acquireResourceRes;
        try {
            acquireResourceRes = this.cloudRecordingClient
                    .mixScenario()
                    .acquire(cname, uid, AcquireMixRecordingResourceClientReq.builder()
                            .resourceExpiredHour(1)
                            .build()).block();
        } catch (AgoraException e) {
            logger.error("Failed to acquire resource,err:{}", e.getMessage());
            return;
        } catch (Exception e) {
            logger.error("Unknown exception,err:{}", e.getMessage());
            return;
        }

        logger.info("Acquire resource successfully,acquireResourceResp:{}", acquireResourceRes);

        if (acquireResourceRes == null) {
            logger.error("Failed to acquire resource, acquireResourceRes is null");
            return;
        }

        // start
        StartResourceRes startResourceRes;

        try {
            startResourceRes = this.cloudRecordingClient
                    .mixScenario()
                    .start(cname, uid, acquireResourceRes.getResourceId(), StartMixRecordingResourceClientReq.builder()
                            .token(token)
                            .recordingConfig(StartResourceReq.RecordingConfig.builder()
                                    .channelType(1)
                                    .streamTypes(2)
                                    .maxIdleTime(30)
                                    .audioProfile(2)
                                    .transcodingConfig(StartResourceReq.TranscodingConfig.builder()
                                            .width(640)
                                            .height(480)
                                            .fps(15)
                                            .bitrate(800)
                                            .mixedVideoLayout(0)
                                            .backgroundColor("#000000")
                                            .build())
                                    .subscribeAudioUIDs(Collections.singletonList("#allstream#"))
                                    .subscribeVideoUIDs(Collections.singletonList("#allstream#"))
                                    .build())
                            .recordingFileConfig(StartResourceReq.RecordingFileConfig.builder()
                                    .avFileType(Collections.singletonList("hls"))
                                    .build())
                            .storageConfig(storageConfig)
                            .build())
                    .block();

        } catch (AgoraException e) {
            logger.error("Failed to start resource,err:{}", e.getMessage());
            return;
        } catch (Exception e) {
            logger.error("Unknown exception,err:{}", e.getMessage());
            return;
        }

        if (startResourceRes == null) {
            logger.error("Failed to start resource, startResourceRes is null");
            return;
        }

        logger.info("Start resource successfully,startResourceResp:{}", startResourceRes);


        // query resource detail
        for (int i = 0; i < 3; i++) {
            QueryMixHLSRecordingResourceRes queryMixHLSRecordingResourceRes;
            try {
                queryMixHLSRecordingResourceRes = this.cloudRecordingClient
                        .mixScenario()
                        .queryHLS(acquireResourceRes.getResourceId(), startResourceRes.getSid())
                        .block();
            } catch (AgoraException e) {
                logger.error("Failed to query resource,err:{}", e.getMessage());
                return;
            } catch (Exception e) {
                logger.error("Unknown exception,err:{}", e.getMessage());
                return;
            }
            if (queryMixHLSRecordingResourceRes == null) {
                logger.error("Failed to query resource, queryMixHLSRecordingResourceRes is null");
                return;
            }

            logger.info("Query resource successfully,queryMixHLSRecordingResourceResp:{}", queryMixHLSRecordingResourceRes);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // update
        UpdateResourceRes updateResourceRes;

        try {
            updateResourceRes = this.cloudRecordingClient
                    .mixScenario()
                    .update(cname, uid, acquireResourceRes.getResourceId(), startResourceRes.getSid(), UpdateMixRecordingResourceClientReq.builder()
                            .streamSubscribe(UpdateResourceReq.StreamSubscribe.builder()
                                    .audioUidList(UpdateResourceReq.AudioUIDList.builder()
                                            .subscribeAudioUIDs(Collections.singletonList("#allstream#"))
                                            .build())
                                    .videoUidList(UpdateResourceReq.VideoUIDList.builder()
                                            .subscribeVideoUIDs(Collections.singletonList("#allstream#"))
                                            .build())
                                    .build())
                            .build()).block();

        } catch (AgoraException e) {
            logger.error("Failed to update resource,err:{}", e.getMessage());
            return;
        } catch (Exception e) {
            logger.error("Unknown exception,err:{}", e.getMessage());
            return;
        }

        if (updateResourceRes == null) {
            logger.error("Failed to update resource, updateResourceRes is null");
            return;
        }

        logger.info("Update resource successfully,updateResourceResp:{}", updateResourceRes);


        // update
        UpdateLayoutResourceRes updateLayoutResourceRes;

        try {
            updateLayoutResourceRes = this.cloudRecordingClient
                    .mixScenario()
                    .updateLayout(cname, uid, acquireResourceRes.getResourceId(), startResourceRes.getSid(), UpdateLayoutMixRecordingResourceClientReq.builder()
                            .mixedVideoLayout(1)
                            .backgroundColor("#FF0000")
                            .build()).block();

        } catch (AgoraException e) {
            logger.error("Failed to update layout resource,err:{}", e.getMessage());
            return;
        } catch (Exception e) {
            logger.error("Unknown exception,err:{}", e.getMessage());
            return;
        }

        if (updateLayoutResourceRes == null) {
            logger.error("Failed to update layout resource, updateLayoutResourceRes is null");
            return;
        }

        logger.info("Update resource successfully,updateLayoutResourceResp:{}", updateLayoutResourceRes);

        // query resource detail
        for (int i = 0; i < 3; i++) {
            QueryMixHLSRecordingResourceRes queryMixHLSRecordingResourceRes;
            try {
                queryMixHLSRecordingResourceRes = this.cloudRecordingClient
                        .mixScenario()
                        .queryHLS(acquireResourceRes.getResourceId(), startResourceRes.getSid())
                        .block();
            } catch (AgoraException e) {
                logger.error("Failed to query resource,err:{}", e.getMessage());
                return;
            } catch (Exception e) {
                logger.error("Unknown exception,err:{}", e.getMessage());
                return;
            }
            if (queryMixHLSRecordingResourceRes == null) {
                logger.error("Failed to query resource, queryMixHLSRecordingResourceRes is null");
                return;
            }

            logger.info("Query resource successfully,queryMixHLSRecordingResourceResp:{}", queryMixHLSRecordingResourceRes);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        StopResourceRes stopResourceRes;
        try {
            stopResourceRes = this.cloudRecordingClient
                    .mixScenario()
                    .stop(cname, uid, startResourceRes.getResourceId(), startResourceRes.getSid(), false)
                    .block();
        } catch (AgoraException e) {
            logger.error("Failed to stop resource,err:{}", e.getMessage());
            return;
        } catch (Exception e) {
            logger.error("Unknown exception,err:{}", e.getMessage());
            return;
        }

        if (stopResourceRes == null) {
            logger.error("Failed to stop resource, stopResourceRes is null");
            return;
        }

        logger.info("Stop resource successfully,stopResourceResp:{}", stopResourceRes);
    }

    public void runHLSAndMP4(String token, StartResourceReq.StorageConfig storageConfig) {
        // Run HLS and MP4 implementation

        // acquire
        AcquireResourceRes acquireResourceRes;
        try {
            acquireResourceRes = this.cloudRecordingClient
                    .mixScenario()
                    .acquire(cname, uid, AcquireMixRecordingResourceClientReq.builder()
                            .resourceExpiredHour(1)
                            .build()).block();
        } catch (AgoraException e) {
            logger.error("Failed to acquire resource,err:{}", e.getMessage());
            return;
        } catch (Exception e) {
            logger.error("Unknown exception,err:{}", e.getMessage());
            return;
        }

        logger.info("Acquire resource successfully,acquireResourceResp:{}", acquireResourceRes);

        if (acquireResourceRes == null) {
            logger.error("Failed to acquire resource, acquireResourceRes is null");
            return;
        }

        // start
        StartResourceRes startResourceRes;

        try {
            startResourceRes = this.cloudRecordingClient
                    .mixScenario()
                    .start(cname, uid, acquireResourceRes.getResourceId(), StartMixRecordingResourceClientReq.builder()
                            .token(token)
                            .recordingConfig(StartResourceReq.RecordingConfig.builder()
                                    .channelType(1)
                                    .streamTypes(2)
                                    .maxIdleTime(30)
                                    .audioProfile(2)
                                    .transcodingConfig(StartResourceReq.TranscodingConfig.builder()
                                            .width(640)
                                            .height(480)
                                            .fps(15)
                                            .bitrate(800)
                                            .mixedVideoLayout(0)
                                            .backgroundColor("#000000")
                                            .build())
                                    .subscribeAudioUIDs(Collections.singletonList("#allstream#"))
                                    .subscribeVideoUIDs(Collections.singletonList("#allstream#"))
                                    .build())
                            .recordingFileConfig(StartResourceReq.RecordingFileConfig.builder()
                                    .avFileType(Arrays.asList("hls", "mp4"))
                                    .build())
                            .storageConfig(storageConfig)
                            .build())
                    .block();

        } catch (AgoraException e) {
            logger.error("Failed to start resource,err:{}", e.getMessage());
            return;
        } catch (Exception e) {
            logger.error("Unknown exception,err:{}", e.getMessage());
            return;
        }

        if (startResourceRes == null) {
            logger.error("Failed to start resource, startResourceRes is null");
            return;
        }

        logger.info("Start resource successfully,startResourceResp:{}", startResourceRes);


        // query resource detail
        for (int i = 0; i < 3; i++) {
            QueryMixHLSAndMP4RecordingResourceRes queryMixHLSAndMP4RecordingResourceRes;
            try {
                queryMixHLSAndMP4RecordingResourceRes = this.cloudRecordingClient
                        .mixScenario()
                        .queryHLSAndMP4(acquireResourceRes.getResourceId(), startResourceRes.getSid())
                        .block();
            } catch (AgoraException e) {
                logger.error("Failed to query resource,err:{}", e.getMessage());
                return;
            } catch (Exception e) {
                logger.error("Unknown exception,err:{}", e.getMessage());
                return;
            }
            if (queryMixHLSAndMP4RecordingResourceRes == null) {
                logger.error("Failed to query resource, queryMixHLSAndMP4RecordingResourceRes is null");
                return;
            }

            logger.info("Query resource successfully,queryMixHLSAndMP4RecordingResourceResp:{}", queryMixHLSAndMP4RecordingResourceRes);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // update
        UpdateResourceRes updateResourceRes;

        try {
            updateResourceRes = this.cloudRecordingClient
                    .mixScenario()
                    .update(cname, uid, acquireResourceRes.getResourceId(), startResourceRes.getSid(), UpdateMixRecordingResourceClientReq.builder()
                            .streamSubscribe(UpdateResourceReq.StreamSubscribe.builder()
                                    .audioUidList(UpdateResourceReq.AudioUIDList.builder()
                                            .subscribeAudioUIDs(Collections.singletonList("#allstream#"))
                                            .build())
                                    .videoUidList(UpdateResourceReq.VideoUIDList.builder()
                                            .subscribeVideoUIDs(Collections.singletonList("#allstream#"))
                                            .build())
                                    .build())
                            .build()).block();

        } catch (AgoraException e) {
            logger.error("Failed to update resource,err:{}", e.getMessage());
            return;
        } catch (Exception e) {
            logger.error("Unknown exception,err:{}", e.getMessage());
            return;
        }

        if (updateResourceRes == null) {
            logger.error("Failed to update resource, updateResourceRes is null");
            return;
        }

        logger.info("Update resource successfully,updateResourceResp:{}", updateResourceRes);


        // update
        UpdateLayoutResourceRes updateLayoutResourceRes;

        try {
            updateLayoutResourceRes = this.cloudRecordingClient
                    .mixScenario()
                    .updateLayout(cname, uid, acquireResourceRes.getResourceId(), startResourceRes.getSid(), UpdateLayoutMixRecordingResourceClientReq.builder()
                            .mixedVideoLayout(1)
                            .backgroundColor("#FF0000")
                            .build()).block();

        } catch (AgoraException e) {
            logger.error("Failed to update layout resource,err:{}", e.getMessage());
            return;
        } catch (Exception e) {
            logger.error("Unknown exception,err:{}", e.getMessage());
            return;
        }

        if (updateLayoutResourceRes == null) {
            logger.error("Failed to update layout resource, updateLayoutResourceRes is null");
            return;
        }

        logger.info("Update resource successfully,updateLayoutResourceResp:{}", updateLayoutResourceRes);

        // query resource detail
        for (int i = 0; i < 3; i++) {
            QueryMixHLSAndMP4RecordingResourceRes queryMixHLSAndMP4RecordingResourceRes;
            try {
                queryMixHLSAndMP4RecordingResourceRes = this.cloudRecordingClient
                        .mixScenario()
                        .queryHLSAndMP4(acquireResourceRes.getResourceId(), startResourceRes.getSid())
                        .block();
            } catch (AgoraException e) {
                logger.error("Failed to query resource,err:{}", e.getMessage());
                return;
            } catch (Exception e) {
                logger.error("Unknown exception,err:{}", e.getMessage());
                return;
            }
            if (queryMixHLSAndMP4RecordingResourceRes == null) {
                logger.error("Failed to query resource, queryMixHLSAndMP4RecordingResourceRes is null");
                return;
            }

            logger.info("Query resource successfully,queryMixHLSAndMP4RecordingResourceResp:{}", queryMixHLSAndMP4RecordingResourceRes);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        StopResourceRes stopResourceRes;
        try {
            stopResourceRes = this.cloudRecordingClient
                    .mixScenario()
                    .stop(cname, uid, startResourceRes.getResourceId(), startResourceRes.getSid(), false)
                    .block();
        } catch (AgoraException e) {
            logger.error("Failed to stop resource,err:{}", e.getMessage());
            return;
        } catch (Exception e) {
            logger.error("Unknown exception,err:{}", e.getMessage());
            return;
        }

        if (stopResourceRes == null) {
            logger.error("Failed to stop resource, stopResourceRes is null");
            return;
        }

        logger.info("Stop resource successfully,stopResourceResp:{}", stopResourceRes);
    }
}
