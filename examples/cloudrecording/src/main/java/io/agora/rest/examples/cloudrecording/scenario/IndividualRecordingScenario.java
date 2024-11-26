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
import io.agora.rest.services.cloudrecording.scenario.individual.req.AcquireIndividualResourceClientReq;
import io.agora.rest.services.cloudrecording.scenario.individual.req.StartIndividualRecordingClientReq;
import io.agora.rest.services.cloudrecording.scenario.individual.req.UpdateIndividualRecordingResourceClientReq;
import io.agora.rest.services.cloudrecording.scenario.individual.res.QueryIndividualRecordingResourceRes;
import io.agora.rest.services.cloudrecording.scenario.individual.res.QueryIndividualRecordingVideoScreenshotResourceRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class IndividualRecordingScenario extends BaseScenario {
    private static final Logger logger = LoggerFactory.getLogger(IndividualRecordingScenario.class);

    public IndividualRecordingScenario(RegionArea region, String appId, String cname, String uid, Credential credential) {
        super(region, appId, cname, uid, credential);
    }

    public void runRecording(String token, StartResourceReq.StorageConfig storageConfig) {
        // acquire
        AcquireResourceRes acquireResourceRes;
        try {
            acquireResourceRes = this.cloudRecordingService
                    .individualScenario()
                    .acquire(cname, uid, false, AcquireIndividualResourceClientReq.builder()
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
            startResourceRes = this.cloudRecordingService
                    .individualScenario()
                    .start(cname, uid, acquireResourceRes.getResourceId(), StartIndividualRecordingClientReq.builder()
                            .token(token)
                            .recordingConfig(StartResourceReq.RecordingConfig.builder()
                                    .channelType(1)
                                    .streamTypes(2)
                                    .maxIdleTime(30)
                                    .subscribeAudioUIDs(Collections.singletonList("#allstream#"))
                                    .subscribeVideoUIDs(Collections.singletonList("#allstream#"))
                                    .subscribeUidGroup(0)
                                    .build())
                            .recordingFileConfig(StartResourceReq.RecordingFileConfig.builder()
                                    .avFileType(Collections.singletonList("hls"))
                                    .build())
                            .storageConfig(storageConfig)
                            .build()).block();
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
            QueryIndividualRecordingResourceRes queryIndividualRecordingResourceRes;
            try {
                queryIndividualRecordingResourceRes = this.cloudRecordingService
                        .individualScenario()
                        .query(acquireResourceRes.getResourceId(), startResourceRes.getSid())
                        .block();
            } catch (AgoraException e) {
                logger.error("Failed to query resource,err:{}", e.getMessage());
                return;
            } catch (Exception e) {
                logger.error("Unknown exception,err:{}", e.getMessage());
                return;
            }
            if (queryIndividualRecordingResourceRes == null) {
                logger.error("Failed to query resource, queryIndividualRecordingResourceRes is null");
                return;
            }

            logger.info("Query resource successfully,queryIndividualRecordingResourceResp:{}", queryIndividualRecordingResourceRes);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // update
        UpdateResourceRes updateResourceRes;

        try {
            updateResourceRes = this.cloudRecordingService
                    .individualScenario()
                    .update(cname, uid, acquireResourceRes.getResourceId(), startResourceRes.getSid(), UpdateIndividualRecordingResourceClientReq.builder()
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

        // query resource detail
        for (int i = 0; i < 3; i++) {
            QueryIndividualRecordingResourceRes queryIndividualRecordingResourceRes;
            try {
                queryIndividualRecordingResourceRes = this.cloudRecordingService
                        .individualScenario()
                        .query(acquireResourceRes.getResourceId(), startResourceRes.getSid())
                        .block();
            } catch (AgoraException e) {
                logger.error("Failed to query resource,err:{}", e.getMessage());
                return;
            } catch (Exception e) {
                logger.error("Unknown exception,err:{}", e.getMessage());
                return;
            }
            if (queryIndividualRecordingResourceRes == null) {
                logger.error("Failed to query resource, queryIndividualRecordingResourceRes is null");
                return;
            }

            logger.info("Query resource successfully,queryIndividualRecordingResourceResp:{}", queryIndividualRecordingResourceRes);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        StopResourceRes stopResourceRes;
        try {
            stopResourceRes = this.cloudRecordingService
                    .individualScenario()
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

    public void runSnapshot(String token, StartResourceReq.StorageConfig storageConfig) {
        // Run snapshot implementation

        // acquire
        AcquireResourceRes acquireResourceRes;
        try {
            acquireResourceRes = this.cloudRecordingService
                    .individualScenario()
                    .acquire(cname, uid, false, AcquireIndividualResourceClientReq.builder()
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
            startResourceRes = this.cloudRecordingService
                    .individualScenario()
                    .start(cname, uid, acquireResourceRes.getResourceId(), StartIndividualRecordingClientReq.builder()
                            .token(token)
                            .recordingConfig(StartResourceReq.RecordingConfig.builder()
                                    .channelType(1)
                                    .streamTypes(2)
                                    .maxIdleTime(30)
                                    .subscribeAudioUIDs(Collections.singletonList("#allstream#"))
                                    .subscribeVideoUIDs(Collections.singletonList("#allstream#"))
                                    .subscribeUidGroup(0)
                                    .build())
                            .snapshotConfig(StartResourceReq.SnapshotConfig.builder()
                                    .captureInterval(5)
                                    .fileType(Collections.singletonList("jpg"))
                                    .build())
                            .storageConfig(storageConfig)
                            .build()).block();
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
            QueryIndividualRecordingVideoScreenshotResourceRes queryIndividualRecordingVideoScreenshotResourceRes;
            try {
                queryIndividualRecordingVideoScreenshotResourceRes = this.cloudRecordingService
                        .individualScenario()
                        .queryVideoScreenshot(acquireResourceRes.getResourceId(), startResourceRes.getSid())
                        .block();
            } catch (AgoraException e) {
                logger.error("Failed to query resource,err:{}", e.getMessage());
                return;
            } catch (Exception e) {
                logger.error("Unknown exception,err:{}", e.getMessage());
                return;
            }
            if (queryIndividualRecordingVideoScreenshotResourceRes == null) {
                logger.error("Failed to query resource, queryIndividualRecordingVideoScreenshotResourceRes is null");
                return;
            }

            logger.info("Query resource successfully,queryIndividualRecordingVideoScreenshotResourceResp:{}", queryIndividualRecordingVideoScreenshotResourceRes);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // update
        UpdateResourceRes updateResourceRes;

        try {
            updateResourceRes = this.cloudRecordingService
                    .individualScenario()
                    .update(cname, uid, acquireResourceRes.getResourceId(), startResourceRes.getSid(), UpdateIndividualRecordingResourceClientReq.builder()
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

        // query resource detail
        for (int i = 0; i < 3; i++) {
            QueryIndividualRecordingVideoScreenshotResourceRes queryIndividualRecordingVideoScreenshotResourceRes;
            try {
                queryIndividualRecordingVideoScreenshotResourceRes = this.cloudRecordingService
                        .individualScenario()
                        .queryVideoScreenshot(acquireResourceRes.getResourceId(), startResourceRes.getSid())
                        .block();
            } catch (AgoraException e) {
                logger.error("Failed to query resource,err:{}", e.getMessage());
                return;
            } catch (Exception e) {
                logger.error("Unknown exception,err:{}", e.getMessage());
                return;
            }
            if (queryIndividualRecordingVideoScreenshotResourceRes == null) {
                logger.error("Failed to query resource, queryIndividualRecordingVideoScreenshotResourceRes is null");
                return;
            }

            logger.info("Query resource successfully,queryIndividualRecordingVideoScreenshotResourceResp:{}", queryIndividualRecordingVideoScreenshotResourceRes);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        StopResourceRes stopResourceRes;
        try {
            stopResourceRes = this.cloudRecordingService
                    .individualScenario()
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

    public void runRecordingAndSnapshot(String token, StartResourceReq.StorageConfig storageConfig) {
        // Run recording and snapshot implementation

        // acquire
        AcquireResourceRes acquireResourceRes;
        try {
            acquireResourceRes = this.cloudRecordingService
                    .individualScenario()
                    .acquire(cname, uid, false, AcquireIndividualResourceClientReq.builder()
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
            startResourceRes = this.cloudRecordingService
                    .individualScenario()
                    .start(cname, uid, acquireResourceRes.getResourceId(), StartIndividualRecordingClientReq.builder()
                            .token(token)
                            .recordingConfig(StartResourceReq.RecordingConfig.builder()
                                    .channelType(1)
                                    .streamTypes(2)
                                    .maxIdleTime(30)
                                    .subscribeAudioUIDs(Collections.singletonList("#allstream#"))
                                    .subscribeVideoUIDs(Collections.singletonList("#allstream#"))
                                    .subscribeUidGroup(0)
                                    .build())
                            .recordingFileConfig(StartResourceReq.RecordingFileConfig.builder()
                                    .avFileType(Collections.singletonList("hls"))
                                    .build())
                            .snapshotConfig(StartResourceReq.SnapshotConfig.builder()
                                    .captureInterval(5)
                                    .fileType(Collections.singletonList("jpg"))
                                    .build())
                            .storageConfig(storageConfig)
                            .build()).block();
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
            QueryIndividualRecordingResourceRes queryIndividualRecordingResourceRes;
            try {
                queryIndividualRecordingResourceRes = this.cloudRecordingService
                        .individualScenario()
                        .query(acquireResourceRes.getResourceId(), startResourceRes.getSid())
                        .block();
            } catch (AgoraException e) {
                logger.error("Failed to query resource,err:{}", e.getMessage());
                return;
            } catch (Exception e) {
                logger.error("Unknown exception,err:{}", e.getMessage());
                return;
            }
            if (queryIndividualRecordingResourceRes == null) {
                logger.error("Failed to query resource, queryIndividualRecordingResourceRes is null");
                return;
            }

            logger.info("Query resource successfully,queryIndividualRecordingResourceResp:{}", queryIndividualRecordingResourceRes);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // update
        UpdateResourceRes updateResourceRes;

        try {
            updateResourceRes = this.cloudRecordingService
                    .individualScenario()
                    .update(cname, uid, acquireResourceRes.getResourceId(), startResourceRes.getSid(), UpdateIndividualRecordingResourceClientReq.builder()
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

        // query resource detail
        for (int i = 0; i < 3; i++) {
            QueryIndividualRecordingResourceRes queryIndividualRecordingResourceRes;
            try {
                queryIndividualRecordingResourceRes = this.cloudRecordingService
                        .individualScenario()
                        .query(acquireResourceRes.getResourceId(), startResourceRes.getSid())
                        .block();
            } catch (AgoraException e) {
                logger.error("Failed to query resource,err:{}", e.getMessage());
                return;
            } catch (Exception e) {
                logger.error("Unknown exception,err:{}", e.getMessage());
                return;
            }
            if (queryIndividualRecordingResourceRes == null) {
                logger.error("Failed to query resource, queryIndividualRecordingResourceRes is null");
                return;
            }

            logger.info("Query resource successfully,queryIndividualRecordingResourceResp:{}", queryIndividualRecordingResourceRes);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        StopResourceRes stopResourceRes;
        try {
            stopResourceRes = this.cloudRecordingService
                    .individualScenario()
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

    public void runRecordingAndPostponeTranscoding(String token, StartResourceReq.StorageConfig storageConfig) {
        // Run recording and postpone transcoding implementation

        // acquire
        AcquireResourceRes acquireResourceRes;
        try {
            acquireResourceRes = this.cloudRecordingService
                    .individualScenario()
                    .acquire(cname, uid, true, AcquireIndividualResourceClientReq.builder()
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
            startResourceRes = this.cloudRecordingService
                    .individualScenario()
                    .start(cname, uid, acquireResourceRes.getResourceId(), StartIndividualRecordingClientReq.builder()
                            .token(token)
                            .recordingConfig(StartResourceReq.RecordingConfig.builder()
                                    .channelType(1)
                                    .streamTypes(2)
                                    .maxIdleTime(30)
                                    .subscribeAudioUIDs(Collections.singletonList("#allstream#"))
                                    .subscribeVideoUIDs(Collections.singletonList("#allstream#"))
                                    .subscribeUidGroup(0)
                                    .build())
                            .recordingFileConfig(StartResourceReq.RecordingFileConfig.builder()
                                    .avFileType(Collections.singletonList("hls"))
                                    .build())
                            .appsCollection(StartResourceReq.AppsCollection.builder()
                                    .combinationPolicy("postpone_transcoding")
                                    .build())
                            .transcodeOptions(StartResourceReq.TranscodeOptions.builder()
                                    .container(StartResourceReq.Container.builder()
                                            .format("mp4")
                                            .build())
                                    .transConfig(StartResourceReq.TransConfig.builder()
                                            .transMode("postponeTranscoding")
                                            .build())
                                    .build())
                            .storageConfig(storageConfig)
                            .build()).block();
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
            QueryIndividualRecordingResourceRes queryIndividualRecordingResourceRes;
            try {
                queryIndividualRecordingResourceRes = this.cloudRecordingService
                        .individualScenario()
                        .query(acquireResourceRes.getResourceId(), startResourceRes.getSid())
                        .block();
            } catch (AgoraException e) {
                logger.error("Failed to query resource,err:{}", e.getMessage());
                return;
            } catch (Exception e) {
                logger.error("Unknown exception,err:{}", e.getMessage());
                return;
            }
            if (queryIndividualRecordingResourceRes == null) {
                logger.error("Failed to query resource, queryIndividualRecordingResourceRes is null");
                return;
            }

            logger.info("Query resource successfully,queryIndividualRecordingResourceResp:{}", queryIndividualRecordingResourceRes);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // update
        UpdateResourceRes updateResourceRes;

        try {
            updateResourceRes = this.cloudRecordingService
                    .individualScenario()
                    .update(cname, uid, acquireResourceRes.getResourceId(), startResourceRes.getSid(), UpdateIndividualRecordingResourceClientReq.builder()
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

        // query resource detail
        for (int i = 0; i < 3; i++) {
            QueryIndividualRecordingResourceRes queryIndividualRecordingResourceRes;
            try {
                queryIndividualRecordingResourceRes = this.cloudRecordingService
                        .individualScenario()
                        .query(acquireResourceRes.getResourceId(), startResourceRes.getSid())
                        .block();
            } catch (AgoraException e) {
                logger.error("Failed to query resource,err:{}", e.getMessage());
                return;
            } catch (Exception e) {
                logger.error("Unknown exception,err:{}", e.getMessage());
                return;
            }
            if (queryIndividualRecordingResourceRes == null) {
                logger.error("Failed to query resource, queryIndividualRecordingResourceRes is null");
                return;
            }

            logger.info("Query resource successfully,queryIndividualRecordingResourceResp:{}", queryIndividualRecordingResourceRes);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        StopResourceRes stopResourceRes;
        try {
            stopResourceRes = this.cloudRecordingService
                    .individualScenario()
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

    public void runRecordingAndAudioMix(String token, StartResourceReq.StorageConfig storageConfig) {
        // Run recording and audio mix implementation

        // acquire
        AcquireResourceRes acquireResourceRes;
        try {
            acquireResourceRes = this.cloudRecordingService
                    .individualScenario()
                    .acquire(cname, uid, true, AcquireIndividualResourceClientReq.builder()
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
            startResourceRes = this.cloudRecordingService
                    .individualScenario()
                    .start(cname, uid, acquireResourceRes.getResourceId(), StartIndividualRecordingClientReq.builder()
                            .token(token)
                            .recordingConfig(StartResourceReq.RecordingConfig.builder()
                                    .channelType(1)
                                    .streamTypes(0)
                                    .streamMode("original")
                                    .maxIdleTime(30)
                                    .subscribeAudioUIDs(Collections.singletonList("#allstream#"))
                                    .subscribeUidGroup(0)
                                    .build())
                            .recordingFileConfig(StartResourceReq.RecordingFileConfig.builder()
                                    .avFileType(Collections.singletonList("hls"))
                                    .build())
                            .appsCollection(StartResourceReq.AppsCollection.builder()
                                    .combinationPolicy("postpone_transcoding")
                                    .build())
                            .transcodeOptions(StartResourceReq.TranscodeOptions.builder()
                                    .container(StartResourceReq.Container.builder()
                                            .format("mp3")
                                            .build())
                                    .transConfig(StartResourceReq.TransConfig.builder()
                                            .transMode("audioMix")
                                            .build())
                                    .audio(StartResourceReq.Audio.builder()
                                            .sampleRate("48000")
                                            .bitRate("48000")
                                            .channels("2")
                                            .build())
                                    .build())
                            .storageConfig(storageConfig)
                            .build()).block();
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
            QueryIndividualRecordingResourceRes queryIndividualRecordingResourceRes;
            try {
                queryIndividualRecordingResourceRes = this.cloudRecordingService
                        .individualScenario()
                        .query(acquireResourceRes.getResourceId(), startResourceRes.getSid())
                        .block();
            } catch (AgoraException e) {
                logger.error("Failed to query resource,err:{}", e.getMessage());
                return;
            } catch (Exception e) {
                logger.error("Unknown exception,err:{}", e.getMessage());
                return;
            }
            if (queryIndividualRecordingResourceRes == null) {
                logger.error("Failed to query resource, queryIndividualRecordingResourceRes is null");
                return;
            }

            logger.info("Query resource successfully,queryIndividualRecordingResourceResp:{}", queryIndividualRecordingResourceRes);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // update
        UpdateResourceRes updateResourceRes;

        try {
            updateResourceRes = this.cloudRecordingService
                    .individualScenario()
                    .update(cname, uid, acquireResourceRes.getResourceId(), startResourceRes.getSid(), UpdateIndividualRecordingResourceClientReq.builder()
                            .streamSubscribe(UpdateResourceReq.StreamSubscribe.builder()
                                    .audioUidList(UpdateResourceReq.AudioUIDList.builder()
                                            .subscribeAudioUIDs(Collections.singletonList("#allstream#"))
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

        // query resource detail
        for (int i = 0; i < 3; i++) {
            QueryIndividualRecordingResourceRes queryIndividualRecordingResourceRes;
            try {
                queryIndividualRecordingResourceRes = this.cloudRecordingService
                        .individualScenario()
                        .query(acquireResourceRes.getResourceId(), startResourceRes.getSid())
                        .block();
            } catch (AgoraException e) {
                logger.error("Failed to query resource,err:{}", e.getMessage());
                return;
            } catch (Exception e) {
                logger.error("Unknown exception,err:{}", e.getMessage());
                return;
            }
            if (queryIndividualRecordingResourceRes == null) {
                logger.error("Failed to query resource, queryIndividualRecordingResourceRes is null");
                return;
            }

            logger.info("Query resource successfully,queryIndividualRecordingResourceResp:{}", queryIndividualRecordingResourceRes);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        StopResourceRes stopResourceRes;
        try {
            stopResourceRes = this.cloudRecordingService
                    .individualScenario()
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
