# 云端录制服务
## 服务简介
云端录制是声网为音视频通话和直播研发的录制组件，提供 RESTful API 供开发者实现录制功能，并将录制文件存至第三方云存储。云端录制有稳定可靠、简单易用、成本可控、方案灵活、支持私有化部署等优势，是在线教育、视频会议、金融监管、客户服务场景的理想录制方案。

## 环境准备

- 获取声网App ID -------- [声网Agora - 文档中心 - 如何获取 App ID](https://docs.agora.io/cn/Agora%20Platform/get_appid_token?platform=All%20Platforms#%E8%8E%B7%E5%8F%96-app-id)

  > - 点击创建应用
      >
      >   ![](https://accktvpic.oss-cn-beijing.aliyuncs.com/pic/github_readme/create_app_1.jpg)
  >
  > - 选择你要创建的应用类型
      >
      >   ![](https://accktvpic.oss-cn-beijing.aliyuncs.com/pic/github_readme/create_app_2.jpg)

- 获取App 证书 ----- [声网Agora - 文档中心 - 获取 App 证书](https://docs.agora.io/cn/Agora%20Platform/get_appid_token?platform=All%20Platforms#%E8%8E%B7%E5%8F%96-app-%E8%AF%81%E4%B9%A6)

  > 在声网控制台的项目管理页面，找到你的项目，点击配置。
  > ![](https://fullapp.oss-cn-beijing.aliyuncs.com/scenario_api/callapi/config/1641871111769.png)
  > 点击主要证书下面的复制图标，即可获取项目的 App 证书。
  > ![](https://fullapp.oss-cn-beijing.aliyuncs.com/scenario_api/callapi/config/1637637672988.png)

- 开启云录制服务
  > ![](https://fullapp.oss-cn-beijing.aliyuncs.com/scenario_api/callapi/config/rtm_config1.jpg)
  > ![](https://fullapp.oss-cn-beijing.aliyuncs.com/scenario_api/callapi/config/rtm_config2.jpg)  
  > ![](https://fullapp.oss-cn-beijing.aliyuncs.com/agora-rest-client/go/open_cloud_recording.png)

## API 接口调用示例
### 获取云端录制资源
> 在开始云端录制之前，你需要调用 acquire 方法获取一个 Resource ID。一个 Resource ID 只能用于一次云端录制服务。

需要设置的参数有：
- appId: 声网的项目 AppID
- username: 声网的Basic Auth认证的用户名
- password: 声网的Basic Auth认证的密码
- cname: 频道名
- uid: 用户 UID
- 更多 clientRequest中的参数见 [Acquire](https://doc.shengwang.cn/doc/cloud-recording/restful/cloud-recording/operations/post-v1-apps-appid-cloud_recording-acquire) 接口文档

通过调用`acquire`方法来实现获取云端录制资源
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
                // Optional values are CN, NA, EU, AP, and the client will automatically
                // switch to use the best domain name according to the configured region
                .regionArea(region)
                .build();

        // Initialize CloudRecordingService

        CloudRecordingService cloudRecordingService = CloudRecordingService.create(agoraConfig);

        AcquireResourceReq acquireResourceReq = AcquireResourceReq.builder().cname(cname).uid(uid)
                .clientRequest(AcquireResourceReq.ClientRequest.builder().scene(1)
                        .resourceExpiredHour(24).build())
                .build();

        logger.info("request:{},clientRequest:{}", acquireResourceReq, acquireResourceReq.getClientRequest());

        AcquireResourceRes acquireResourceRes = null;
        try {
            acquireResourceRes = cloudRecordingService.acquire(acquireResourceReq).block();

            assertNotNull(acquireResourceResp);
            logger.info("acquire resource response:{}", acquireResourceRes);
        } catch (AgoraException e) {
            logger.error("Agora  error:{}", e.getMessage());
        } catch (Exception e) {
            logger.error("Internal error:{}", e.getMessage());
        }
```

### 开始云端录制
> 通过 acquire 方法获取云端录制资源后，调用 start 方法开始云端录制。

需要设置的参数有：
- cname: 频道名
- uid: 用户 UID
- resourceId: 云端录制资源ID
- mode: 云端录制模式
- storageConfig: 存储配置
- 更多 clientRequest中的参数见 [Start](https://doc.shengwang.cn/doc/cloud-recording/restful/cloud-recording/operations/post-v1-apps-appid-cloud_recording-resourceid-resourceid-mode-mode-start) 接口文档

通过调用`start`方法来实现开始云端录制
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
            startResourceRes = cloudRecordingService
                    .start(resourceId,mode, startResourceReq)
                    .block();
            logger.info("start resource response:{}", startResourceRes);

        } catch (AgoraException e) {
            logger.error("Agora  error:{}", e.getMessage());
        } catch (Exception e) {
            logger.error("Internal error:{}", e.getMessage());

        }
```

### 停止云端录制
> 开始录制后，你可以调用 stop 方法离开频道，停止录制。录制停止后如需再次录制，必须再调用 acquire 方法请求一个新的 Resource ID。

需要设置的参数有：
- cname: 频道名
- uid: 用户ID
- resourceId: 云端录制资源ID
- sid: 会话ID
- mode: 云端录制模式
- 更多 clientRequest中的参数见 [Stop](https://doc.shengwang.cn/doc/cloud-recording/restful/cloud-recording/operations/post-v1-apps-appid-cloud_recording-resourceid-resourceid-sid-sid-mode-mode-stop) 接口文档

因为Stop 接口返回的不是一个固定的结构体，所以需要根据返回的serverResponseMode来判断具体的返回类型

通过调用`stop`方法来实现停止云端录制
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
            stopResourceRes = cloudRecordingService
                    .stop(resourceId, sid, mode, stopResourceReq)
                    .block();
            logger.info("stop resource response:{}", stopResourceRes);
        } catch (AgoraException e) {
            logger.error("Agora  error:{}", e.getMessage());
        } catch (Exception e) {
            logger.error("Internal error:{}", e.getMessage());
        } 

```

### 查询云端录制状态
> 开始录制后，你可以调用 query 方法查询录制状态。

需要设置的参数有：
- cname: 频道名
- uid: 用户ID
- resourceId: 云端录制资源ID
- sid: 会话ID
- mode: 云端录制模式
- 更多 clientRequest中的参数见[Query](https://doc.shengwang.cn/doc/cloud-recording/restful/cloud-recording/operations/get-v1-apps-appid-cloud_recording-resourceid-resourceid-sid-sid-mode-mode-query)接口文档

因为 Query 接口返回的不是一个固定的结构体，所以需要根据返回的serverResponseMode来判断具体的返回类型

通过调用`query`方法来实现查询云端录制状态
```java
        QueryResourceRes queryResourceRes = null;

        try {
            queryResourceRes = cloudRecordingService
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

### 更新云端录制设置
> 开始录制后，你可以调用 update 方法更新如下录制配置：
> * 对单流录制和合流录制，更新订阅名单。
> * 对页面录制，设置暂停/恢复页面录制，或更新页面录制转推到 CDN 的推流地址（URL）。

需要设置的参数有：
- cname: 频道名
- uid: 用户 UID
- resourceId: 云端录制资源ID
- sid: 会话ID
- mode: 云端录制模式
- 更多 clientRequest中的参数见 [Update](https://doc.shengwang.cn/doc/cloud-recording/restful/cloud-recording/operations/post-v1-apps-appid-cloud_recording-resourceid-resourceid-sid-sid-mode-mode-update) 接口文档

通过调用`update`方法来实现更新云端录制设置
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
            updateResourceRes = cloudRecordingService
                    .update(resourceId, sid, mode, updateResourceReq)
                    .block();
            logger.info("update resource response:{}", updateResourceRes);
        } catch (AgoraException e) {
            logger.error("Agora  error:{}", e.getMessage());
        } catch (Exception e) {
            logger.error("Internal error:{}", e.getMessage());
        }
```

## 错误码和响应状态码处理
具体的业务响应码请参考 [业务响应码](https://doc.shengwang.cn/doc/cloud-recording/restful/response-code) 文档
