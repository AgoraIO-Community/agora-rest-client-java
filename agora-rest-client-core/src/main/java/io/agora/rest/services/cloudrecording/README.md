# Cloud Recording Service

English | [简体中文](./README_ZH.md)

## Service Introduction
Cloud Recording is a recording component developed by Agora for audio and video calls and live broadcasts. It provides RESTful APIs for developers to implement recording functionality and store recording files in third-party cloud storage. Cloud Recording offers advantages such as stability, reliability, ease of use, cost control, flexible solutions, and support for private deployment, making it an ideal recording solution for online education, video conferences, financial supervision, and customer service scenarios.

## Environment Preparation

- Obtain Agora App ID -------- [Agora Console](https://console.agora.io/v2)

  > - Click Create Application
  >
  >   ![](../../../../../../../../../assets/imges/EN/create_app_1.png)
  >
  > - Select the type of application you want to create
  >
  >   ![](../../../../../../../../../assets/imges/EN/create_app_2.png)

- Obtain App Certificate ----- [Agora Console](https://console.agora.io/v2)

  > In the project management page of the Agora Console, find your project and click Configure.
  > ![](../../../../../../../../../assets/imges/EN/config_app.png)
  > Click the copy icon under Primary Certificate to obtain the App Certificate for your project.
  > ![](../../../../../../../../../assets/imges/EN/copy_app_cert.png)

- Check the status of the recording service
  > ![](../../../../../../../../../assets/imges/EN/open_cloud_recording.png)

## API Call Examples
### Acquire Cloud Recording Resources
> Before starting cloud recording, you need to call the acquire method to obtain a Resource ID. A Resource ID can only be used for one cloud recording service.

Required parameters:
- appId: Agora project AppID
- username: Username for Agora Basic Auth authentication
- password: Password for Agora Basic Auth authentication
- cname: Channel name
- uid: User UID
- For more parameters in clientRequest, see the [Acquire](https://docs.agora.io/en/cloud-recording/reference/restful-api#acquire) API documentation

Implement acquiring cloud recording resources by calling the `acquire` method
```java
        String appId = "";
        String cname = "";
        String uid = "";
        String username = "";
        String password = "";

        Credential basicAuthCredential = new BasicAuthCredential(username, password);
        
        // Initialize AgoraConfig
        AgoraConfig agoraConfig = AgoraConfig.builder()
                .appId(appId)
                .credential(credential)
                // Specify the region where the server is located. 
                // Optional values are CN, US, EU, AP, and the client will automatically
                // switch to use the best domain name according to the configured region
                .domainArea(DomainArea.CN)
                .build();

        // Initialize CloudRecordingClient

        CloudRecordingClient cloudRecordingClient = CloudRecordingClient.create(agoraConfig);

        AcquireResourceReq acquireResourceReq = AcquireResourceReq.builder().cname(cname).uid(uid)
                .clientRequest(AcquireResourceReq.ClientRequest.builder().scene(1)
                        .resourceExpiredHour(24).build())
                .build();

        logger.info("request:{},clientRequest:{}", acquireResourceReq, acquireResourceReq.getClientRequest());

        AcquireResourceRes acquireResourceRes = null;
        try {
            acquireResourceRes = cloudRecordingClient.acquire(acquireResourceReq).block();

            assertNotNull(acquireResourceResp);
            logger.info("acquire resource response:{}", acquireResourceRes);
        } catch (AgoraException e) {
            logger.error("Agora  error:{}", e.getMessage());
        } catch (Exception e) {
            logger.error("Internal error:{}", e.getMessage());
        }
```

### Start Cloud Recording
> After acquiring cloud recording resources through the acquire method, call the start method to begin cloud recording.

Required parameters:
- cname: Channel name
- uid: User UID
- resourceId: Cloud recording resource ID
- mode: Cloud recording mode
- storageConfig: Storage configuration
- For more parameters in clientRequest, see the [Start](https://docs.agora.io/en/cloud-recording/reference/restful-api#start) API documentation

Implement starting cloud recording by calling the `start` method
```java
     StartResourceReq.StorageConfig storageConfig = StartResourceReq.StorageConfig.builder()
                .accessKey("")
                .secretKey("")
                .fileNamePrefix(Collections.singletonList(""))
                .bucket("")
                .vendor(2)
                .region(3)
                .build();

        StartResourceReq startResourceReq = StartResourceReq.builder()
                .cname(cname)
                .uid(uid)
                .clientRequest(StartResourceReq.StartClientRequest.builder()
                        .recordingFileConfig(StartResourceReq.RecordingFileConfig.builder()
                                .avFileType(Arrays.asList("hls", "mp4"))
                                .build())
                        .storageConfig(storageConfig)
                        .extensionServiceConfig(StartResourceReq.ExtensionServiceConfig
                                .builder()
                                .errorHandlePolicy("error_abort")
                                .extensionServices(Arrays.asList(
                                        StartResourceReq.ExtensionService
                                                .builder()
                                                .serviceParam(StartResourceReq.WebRecordingServiceParam
                                                        .builder()
                                                        .url("https://www.example.com")
                                                        .audioProfile(2)
                                                        .videoWidth(1280)
                                                        .videoHeight(720)
                                                        .maxRecordingHour(
                                                                1)
                                                        .build())
                                                .errorHandlePolicy(
                                                        "error_abort")
                                                .serviceName("web_recorder_service")
                                                .build(),
                                        StartResourceReq.ExtensionService
                                                .builder()
                                                .serviceParam(StartResourceReq.RtmpPublishServiceParam
                                                        .builder()
                                                        .outputs(Collections
                                                                .singletonList(StartResourceReq.Outputs
                                                                        .builder()
                                                                        .rtmpUrl(
                                                                                "rtmp://xxx.xxx.xxx.xxx:1935/live/test")
                                                                        .build()))
                                                        .build())
                                                .serviceName("rtmp_publish_service")
                                                .errorHandlePolicy(
                                                        "error_abort")
                                                .build()))
                                .build())
                        .build())
                .build();

        StartResourceRes startResourceRes = null;

        try {
            startResourceRes = cloudRecordingClient
                    .start(resourceId,mode, startResourceReq)
                    .block();
            logger.info("start resource response:{}", startResourceRes);

        } catch (AgoraException e) {
            logger.error("Agora  error:{}", e.getMessage());
        } catch (Exception e) {
            logger.error("Internal error:{}", e.getMessage());

        }
```

### Stop Cloud Recording
> After starting recording, you can call the stop method to leave the channel and stop recording. If you need to record again after stopping, you must call the acquire method again to request a new Resource ID.

Required parameters:
- cname: Channel name
- uid: User ID
- resourceId: Cloud recording resource ID
- sid: Session ID
- mode: Cloud recording mode
- For more parameters in clientRequest, see the [Stop](https://docs.agora.io/en/cloud-recording/reference/restful-api#stop) API documentation

Since the Stop interface does not return a fixed structure, you need to determine the specific return type based on the serverResponseMode returned

Implement stopping cloud recording by calling the `stop` method
```java
   StopResourceReq stopResourceReq = StopResourceReq.builder()
                .cname(cname)
                .uid(uid)
                .clientRequest(StopResourceReq.StopClientRequest.builder()
                        .asyncStop(true)
                        .build())
                .build();

        StopResourceRes stopResourceRes;
        try {
            stopResourceRes = cloudRecordingClient
                    .stop(resourceId, sid, mode, stopResourceReq)
                    .block();
            logger.info("stop resource response:{}", stopResourceRes);
        } catch (AgoraException e) {
            logger.error("Agora  error:{}", e.getMessage());
        } catch (Exception e) {
            logger.error("Internal error:{}", e.getMessage());
        } 

```

### Query Cloud Recording Status
> After starting recording, you can call the query method to check the recording status.

Required parameters:
- cname: Channel name
- uid: User ID
- resourceId: Cloud recording resource ID
- sid: Session ID
- mode: Cloud recording mode
- For more parameters in clientRequest, see the [Query](https://docs.agora.io/en/cloud-recording/reference/restful-api#query) API documentation

Since the Query interface does not return a fixed structure, you need to determine the specific return type based on the serverResponseMode returned

Implement querying cloud recording status by calling the `query` method
```java
        QueryResourceRes queryResourceRes = null;

        try {
            queryResourceRes = cloudRecordingClient
                    .query(resourceId, sid,mode)
                    .block();

            logger.info("query resource response:{}", queryResourceRes);
            switch (queryResourceRes.getServerResponseType()) {
                case QUERY_SERVER_RESPONSE_UNKNOWN_TYPE:
                    logger.error("Unknown server response type");
                    break;
                case QUERY_INDIVIDUAL_RECORDING_SERVER_RESPONSE_TYPE:
                    logger.info("individual recording server response:{}",
                            queryResourceRes.getQueryIndividualRecordingServerResponse());
                    break;
                case QUERY_INDIVIDUAL_VIDEO_SCREENSHOT_SERVER_RESPONSE_TYPE:
                    logger.info("individual video screenshot server response:{}",
                            queryResourceRes.getQueryIndividualVideoScreenshotServerResponse());
                    break;
                case QUERY_MIX_RECORDING_HLS_SERVER_RESPONSE_TYPE:
                    logger.info("mix recording hls server response:{}",
                            queryResourceRes.getMixRecordingHLSServerResponse());
                    break;
                case QUERY_MIX_RECORDING_HLS_AND_MP4_SERVER_RESPONSE_TYPE:
                    logger.info("mix recording hls and mp4 server response:{}",
                            queryResourceRes.getMixRecordingHLSAndMP4ServerResponse());
                    break;
                case QUERY_WEB_RECORDING_SERVER_RESPONSE_TYPE:
                    logger.info("web recording server response:{}",
                            queryResourceRes.getWebRecordingServerResponse());
                    break;
            }
        } catch (AgoraException e) {
            logger.error("Agora  error:{}", e.getMessage());
        } catch (Exception e) {
            logger.error("Internal error:{}", e.getMessage());
        }
```

### Update Cloud Recording Settings
> After starting recording, you can call the update method to update the following recording configurations:
> * For individual recording and composite recording, update the subscription list.
> * For web recording, set pause/resume web recording, or update the streaming URL for pushing web recording to CDN.

Required parameters:
- cname: Channel name
- uid: User UID
- resourceId: Cloud recording resource ID
- sid: Session ID
- mode: Cloud recording mode
- For more parameters in clientRequest, see the [Update](https://docs.agora.io/en/cloud-recording/reference/restful-api#update) API documentation

Implement updating cloud recording settings by calling the `update` method
```java
  UpdateResourceReq updateResourceReq = UpdateResourceReq.builder()
                .uid(uid)
                .cname(cname)
                .clientRequest(UpdateResourceReq.ClientRequest.builder()
                        .webRecordingConfig(UpdateResourceReq.WebRecordingConfig.builder()
                                .onHold(true)
                                .build())
                        .rtmpPublishConfig(UpdateResourceReq.RtmpPublishConfig.builder()
                                .outputs(Collections.singletonList(
                                        UpdateResourceReq.UpdateOutput.builder()
                                                .rtmpURL("rtmp://yyy.yyy.yyy.yyy:1935/live/test")
                                                .build()))
                                .build())
                        .build())
                .build();

        UpdateResourceRes updateResourceRes;
        try {
            updateResourceRes = cloudRecordingClient
                    .update(resourceId, sid, mode, updateResourceReq)
                    .block();
            logger.info("update resource response:{}", updateResourceRes);
        } catch (AgoraException e) {
            logger.error("Agora  error:{}", e.getMessage());
        } catch (Exception e) {
            logger.error("Internal error:{}", e.getMessage());
        }
```

## Error Codes and Response Status Codes
For specific business response codes, please refer to the [Business Response Codes](https://docs.agora.io/en/cloud-recording/reference/common-errors) documentation