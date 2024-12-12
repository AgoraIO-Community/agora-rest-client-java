package io.agora.rest.examples.cloudrecording.scenario;

import io.agora.rest.AgoraException;
import io.agora.rest.core.Credential;
import io.agora.rest.core.RegionArea;
import io.agora.rest.services.cloudrecording.api.req.StartResourceReq;
import io.agora.rest.services.cloudrecording.api.req.UpdateResourceReq;
import io.agora.rest.services.cloudrecording.api.res.AcquireResourceRes;
import io.agora.rest.services.cloudrecording.api.res.StartResourceRes;
import io.agora.rest.services.cloudrecording.api.res.StopResourceRes;
import io.agora.rest.services.cloudrecording.api.res.UpdateResourceRes;
import io.agora.rest.services.cloudrecording.scenario.web.req.AcquireWebRecordingResourceClientReq;
import io.agora.rest.services.cloudrecording.scenario.web.req.StartWebRecordingResourceClientReq;
import io.agora.rest.services.cloudrecording.scenario.web.req.UpdateWebRecordingResourceClientReq;
import io.agora.rest.services.cloudrecording.scenario.web.res.QueryWebRecordingResourceRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;

public class WebRecordingScenario extends BaseScenario {
    private static final Logger logger = LoggerFactory.getLogger(WebRecordingScenario.class);

    public WebRecordingScenario(RegionArea region, String appId, String cname, String uid, Credential credential) {
        super(region, appId, cname, uid, credential);
    }

    public void runWebRecorder(StartResourceReq.StorageConfig storageConfig) {
        // Run web recorder implementation

        // acquire
        AcquireResourceRes acquireResourceRes;
        try {
            acquireResourceRes = this.cloudRecordingClient
                    .webScenario()
                    .acquire(cname, uid, AcquireWebRecordingResourceClientReq.builder()
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
                    .webScenario()
                    .start(cname, uid, acquireResourceRes.getResourceId(),
                            StartWebRecordingResourceClientReq.builder()
                                    .recordingFileConfig(StartResourceReq.RecordingFileConfig.builder()
                                            .avFileType(Arrays.asList("hls", "mp4"))
                                            .build())
                                    .storageConfig(storageConfig)
                                    .extensionServiceConfig(StartResourceReq.ExtensionServiceConfig.builder()
                                            .errorHandlePolicy("error_abort")
                                            .extensionServices(Collections.singletonList(StartResourceReq.ExtensionService.builder()
                                                    .serviceName("web_recorder_service")
                                                    .errorHandlePolicy("error_abort")
                                                    .serviceParam(StartResourceReq.WebRecordingServiceParam.builder()
                                                            .url("https://live.bilibili.com/")
                                                            .audioProfile(2)
                                                            .videoWidth(1280)
                                                            .videoHeight(720)
                                                            .maxRecordingHour(1)
                                                            .build())
                                                    .build()))
                                            .build())
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
            QueryWebRecordingResourceRes queryWebRecordingResourceRes;
            try {
                queryWebRecordingResourceRes = this.cloudRecordingClient
                        .webScenario()
                        .query(acquireResourceRes.getResourceId(), startResourceRes.getSid())
                        .block();
            } catch (AgoraException e) {
                logger.error("Failed to query resource,err:{}", e.getMessage());
                return;
            } catch (Exception e) {
                logger.error("Unknown exception,err:{}", e.getMessage());
                return;
            }
            if (queryWebRecordingResourceRes == null) {
                logger.error("Failed to query resource, queryWebRecordingResourceRes is null");
                return;
            }

            logger.info("Query resource successfully,queryWebRecordingResourceResp:{}", queryWebRecordingResourceRes);
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
                    .webScenario()
                    .update(cname, uid, acquireResourceRes.getResourceId(), startResourceRes.getSid(), UpdateWebRecordingResourceClientReq.builder()
                            .webRecordingConfig(UpdateResourceReq.WebRecordingConfig.builder()
                                    .onHold(false)
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

        // query resource detail
        for (int i = 0; i < 3; i++) {
            QueryWebRecordingResourceRes queryWebRecordingResourceRes;
            try {
                queryWebRecordingResourceRes = this.cloudRecordingClient
                        .webScenario()
                        .query(acquireResourceRes.getResourceId(), startResourceRes.getSid())
                        .block();
            } catch (AgoraException e) {
                logger.error("Failed to query resource,err:{}", e.getMessage());
                return;
            } catch (Exception e) {
                logger.error("Unknown exception,err:{}", e.getMessage());
                return;
            }
            if (queryWebRecordingResourceRes == null) {
                logger.error("Failed to query resource, queryWebRecordingResourceRes is null");
                return;
            }

            logger.info("Query resource successfully,queryWebRecordingResourceResp:{}", queryWebRecordingResourceRes);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        StopResourceRes stopResourceRes;
        try {
            stopResourceRes = this.cloudRecordingClient
                    .webScenario()
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

    public void runWebRecorderAndRtmpPublish(StartResourceReq.StorageConfig storageConfig) {
        // Run web recorder and RTMP publish implementation

        // acquire
        AcquireResourceRes acquireResourceRes;
        try {
            acquireResourceRes = this.cloudRecordingClient
                    .webScenario()
                    .acquire(cname, uid, AcquireWebRecordingResourceClientReq.builder()
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
                    .webScenario()
                    .start(cname, uid, acquireResourceRes.getResourceId(),
                            StartWebRecordingResourceClientReq.builder()
                                    .recordingFileConfig(StartResourceReq.RecordingFileConfig.builder()
                                            .avFileType(Arrays.asList("hls", "mp4"))
                                            .build())
                                    .storageConfig(storageConfig)
                                    .extensionServiceConfig(StartResourceReq.ExtensionServiceConfig.builder()
                                            .errorHandlePolicy("error_abort")
                                            .extensionServices(Arrays.asList(StartResourceReq.ExtensionService.builder()
                                                            .serviceName("web_recorder_service")
                                                            .errorHandlePolicy("error_abort")
                                                            .serviceParam(StartResourceReq.WebRecordingServiceParam.builder()
                                                                    .url("https://live.bilibili.com/")
                                                                    .audioProfile(2)
                                                                    .videoWidth(1280)
                                                                    .videoHeight(720)
                                                                    .maxRecordingHour(1)
                                                                    .build())
                                                            .build(),
                                                    StartResourceReq.ExtensionService.builder()
                                                            .serviceName("rtmp_publish_service")
                                                            .errorHandlePolicy("error_ignore")
                                                            .serviceParam(StartResourceReq.RtmpPublishServiceParam.builder()
                                                                    .outputs(Collections.singletonList(StartResourceReq.Outputs.builder()
                                                                            .rtmpUrl("rtmp://xxx.xxx.xxx.xxx:1935/live/test")
                                                                            .build()))
                                                                    .build())
                                                            .build()
                                            ))
                                            .build())
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
            QueryWebRecordingResourceRes queryWebRecordingResourceRes;
            try {
                queryWebRecordingResourceRes = this.cloudRecordingClient
                        .webScenario()
                        .query(acquireResourceRes.getResourceId(), startResourceRes.getSid())
                        .block();
            } catch (AgoraException e) {
                logger.error("Failed to query resource,err:{}", e.getMessage());
                return;
            } catch (Exception e) {
                logger.error("Unknown exception,err:{}", e.getMessage());
                return;
            }
            if (queryWebRecordingResourceRes == null) {
                logger.error("Failed to query resource, queryWebRecordingResourceRes is null");
                return;
            }

            logger.info("Query resource successfully,queryWebRecordingResourceResp:{}", queryWebRecordingResourceRes);
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
                    .webScenario()
                    .update(cname, uid, acquireResourceRes.getResourceId(), startResourceRes.getSid(), UpdateWebRecordingResourceClientReq.builder()
                            .webRecordingConfig(UpdateResourceReq.WebRecordingConfig.builder()
                                    .onHold(false)
                                    .build())
                            .rtmpPublishConfig(UpdateResourceReq.RtmpPublishConfig.builder()
                                    .outputs(Collections.singletonList(UpdateResourceReq.UpdateOutput.builder()
                                            .rtmpURL("rtmp://yyy.yyy.yyy.yyy:1935/live/test")
                                            .build())
                                    ).build())
                            .build())
                    .block();

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

        // query resource detail
        for (int i = 0; i < 3; i++) {
            QueryWebRecordingResourceRes queryWebRecordingResourceRes;
            try {
                queryWebRecordingResourceRes = this.cloudRecordingClient
                        .webScenario()
                        .query(acquireResourceRes.getResourceId(), startResourceRes.getSid())
                        .block();
            } catch (AgoraException e) {
                logger.error("Failed to query resource,err:{}", e.getMessage());
                return;
            } catch (Exception e) {
                logger.error("Unknown exception,err:{}", e.getMessage());
                return;
            }
            if (queryWebRecordingResourceRes == null) {
                logger.error("Failed to query resource, queryWebRecordingResourceRes is null");
                return;
            }

            logger.info("Query resource successfully,queryWebRecordingResourceResp:{}", queryWebRecordingResourceRes);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        StopResourceRes stopResourceRes;
        try {
            stopResourceRes = this.cloudRecordingClient
                    .webScenario()
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
