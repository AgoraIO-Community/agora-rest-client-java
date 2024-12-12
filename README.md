# Agora REST Client for Java

<p>
<img alt="Maven Central Version" src="https://img.shields.io/maven-central/v/io.agora/agora-rest-client-core?colorB=brightgreen">
<img alt="GitHub License" src="https://img.shields.io/github/license/AgoraIO-Community/agora-rest-client-java">
<a href="https://github.com/AgoraIO-Community/agora-rest-client-java/actions/workflows/maven.yml"><img alt="Java CI with Maven" src="https://github.com/AgoraIO-Community/agora-rest-client-java/actions/workflows/maven.yml/badge.svg"></a>
<img alt="GitHub" src="https://img.shields.io/github/v/release/AgoraIO-Community/agora-rest-client-java">
<img alt="GitHub Issues or Pull Requests" src="https://img.shields.io/github/issues-pr/AgoraIO-Community/agora-rest-client-java">
</p>

`agora-rest-client-java`是用 Java 语言编写的一个开源项目，专门为 Agora REST API 设计。它包含了 Agora 官方提供的 REST
API 接口的包装和内部实现，可以帮助开发者更加方便的集成服务端 Agora REST API。

> 注意： 该 SDK 经过一些测试以确保其基本功能正常运作。然而，由于软件开发的复杂性，我们无法保证它是完全没有缺陷的。
>
>该SDK目前可能存在一些潜在的 BUG 或不稳定性。我们鼓励社区的开发者和用户积极参与，共同改进这个项目。

## 特性

* 封装了 Agora REST API 的请求和响应处理，简化与 Agora REST API 的通信流程
* 当遇到 DNS 解析失败、网络错误或者请求超时等问题的时候，提供了自动切换最佳域名的能力，以保障请求 REST API 服务的可用性
* 提供了易于使用的 API，可轻松地实现调用 Agora REST API 的常见功能，如开启云录制、停止云录制等
* 基于 Java 语言，具有异步性、并发性和可扩展性

## 支持的服务

* [云端录制 Cloud Recording ](./agora-rest-client-core/src/main/java/io/agora/rest/services/cloudrecording/README.md)

## 环境准备

* [Java 1.8 或以上版本](https://www.java.com)
* 在声网 [Console 平台](https://console.shengwang.cn/)申请的 App ID 和 App Certificate
* 在声网 [Console 平台](https://console.shengwang.cn/)的 Basic Auth 认证信息
* 在声网 [Console 平台](https://console.shengwang.cn/)开启相关的服务能力

## 安装

首先，在`poml.xml`文件中添加 REST Client 依赖：

```xml
<dependency>
    <groupId>io.agora</groupId>
    <artifactId>agora-rest-client-core</artifactId>
    <version>0.1.3-BETA</version>
</dependency>
```

## 使用示例

以调用云录制服务为例：

```java
package com.company.example;

import io.agora.rest.AgoraException;
import io.agora.rest.core.AgoraConfig;
import io.agora.rest.core.BasicAuthCredential;
import io.agora.rest.core.Credential;
import io.agora.rest.core.RegionArea;
import io.agora.rest.services.cloudrecording.api.req.StartResourceReq;
import io.agora.rest.services.cloudrecording.api.res.AcquireResourceRes;
import io.agora.rest.services.cloudrecording.api.res.StartResourceRes;
import io.agora.rest.services.cloudrecording.api.res.StopResourceRes;
import io.agora.rest.services.cloudrecording.CloudRecordingClient;
import io.agora.rest.services.cloudrecording.scenario.mix.req.AcquireMixRecordingResourceClientReq;
import io.agora.rest.services.cloudrecording.scenario.mix.req.StartMixRecordingResourceClientReq;
import io.agora.rest.services.cloudrecording.scenario.mix.res.QueryMixHLSAndMP4RecordingResourceRes;

import java.util.Arrays;
import java.util.Collections;

public class Main {
  private static String appId = "<your appId>";

  private static String cname = "<your cname>";

  private static String uid = "<your uid>";

  private static String username = "<the username of basic auth credential>";

  private static String password = "<the password of basic auth credential>";

  private static String token = "<your token>";

  private static String accessKey = "<your accessKey>";

  private static String secretKey = "<your secretKey>";

  private static Integer region = 0; // <your region>

  private static String bucket = "<your bucket>";

  private static Integer vendor = 0; // <your vendor>

  public static void main(String[] args) throws Exception {

    Credential credential = new BasicAuthCredential(username, password);

    // Initialize AgoraConfig
    AgoraConfig agoraConfig = AgoraConfig.builder()
            .appId(appId)
            .credential(credential)
            // Specify the region where the server is located.
            // Optional values are CN, NA, EU, AP, and the client will automatically
            // switch to use the best domain name according to the configured region
            .regionArea(RegionArea.CNRegionArea)
            .build();

    // Initialize CloudRecordingClient

    CloudRecordingClient cloudRecordingClient = CloudRecordingClient.create(agoraConfig);


    AcquireResourceRes acquireResourceRes;

    // Acquire resource
    try {
      acquireResourceRes = cloudRecordingClient
              .mixScenario()
              .acquire(cname, uid, AcquireMixRecordingResourceClientReq.builder()
                      .build())
              .block();
    } catch (AgoraException e) {
      System.out.printf("agora error:%s", e.getMessage());
      return;
    } catch (Exception e) {
      System.out.printf("unknown error:%s", e.getMessage());
      return;
    }

    // Check if the response is null
    if (acquireResourceRes == null || acquireResourceRes.getResourceId() == null) {
      System.out.println("failed to get resource");
      return;
    }


    System.out.printf("resourceId:%s", acquireResourceRes.getResourceId());

    System.out.println("acquire resource success");

    // Define storage config
    StartResourceReq.StorageConfig storageConfig = StartResourceReq.StorageConfig.builder()
            .accessKey(accessKey)
            .secretKey(secretKey)
            .bucket(bucket)
            .vendor(vendor)
            .region(region)
            .build();

    // Define start resource request
    StartMixRecordingResourceClientReq startResourceReq = StartMixRecordingResourceClientReq.builder()
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
            .build();


    StartResourceRes startResourceRes;

    // Start resource
    try {
      startResourceRes = cloudRecordingClient
              .mixScenario()
              .start(cname, uid,
                      acquireResourceRes.getResourceId(),
                      startResourceReq)
              .block();

    } catch (AgoraException e) {
      System.out.printf("agora error:%s", e.getMessage());
      return;
    } catch (Exception e) {
      System.out.printf("unknown error:%s", e.getMessage());
      return;
    }

    // Check if the response is null
    if (startResourceRes == null || startResourceRes.getSid() == null) {
      System.out.println("failed to start resource");
      return;
    }

    System.out.printf("sid:%s", startResourceRes.getSid());

    System.out.println("start resource success");

    Thread.sleep(3000);

    QueryMixHLSAndMP4RecordingResourceRes queryResourceRes;

    // Query resource
    try {
      queryResourceRes = cloudRecordingClient
              .mixScenario()
              .queryHLSAndMP4(startResourceRes.getResourceId(), startResourceRes.getSid())
              .block();

    } catch (AgoraException e) {
      System.out.printf("agora error:%s", e.getMessage());
      return;
    } catch (Exception e) {
      System.out.printf("unknown error:%s", e.getMessage());
      return;
    }

    if (queryResourceRes == null || queryResourceRes.getServerResponse() == null) {
      System.out.println("failed to query resource");
      return;
    }

    System.out.println("query resource success");

    Thread.sleep(3000);

    StopResourceRes stopResourceRes;

    // Stop resource
    try {
      stopResourceRes = cloudRecordingClient
              .mixScenario()
              .stop(cname, uid, startResourceRes.getResourceId(), startResourceRes.getSid(),
                      true)
              .block();
    } catch (AgoraException e) {
      System.out.printf("agora error:%s", e.getMessage());
      return;
    } catch (Exception e) {
      System.out.printf("unknown error:%s", e.getMessage());
      return;
    }

    // Check if the response is null
    if (stopResourceRes == null || stopResourceRes.getSid() == null) {
      System.out.println("failed to stop resource");
    } else {
      System.out.println("stop resource success");
    }

  }
}

```

更多的示例可在 [Example](./examples) 查看

## 集成遇到困难，该如何联系声网获取协助

> 方案1：如果您已经在使用声网服务或者在对接中，可以直接联系对接的销售或服务
>
> 方案2：发送邮件给 [support@agora.io](mailto:support@agora.io) 咨询
>
> 方案3：扫码加入我们的微信交流群提问
>
> <img src="https://download.agora.io/demo/release/SDHY_QA.jpg" width="360" height="360">
---

## 贡献

本项目欢迎并接受贡献。如果您在使用中遇到问题或有改进建议，请提出 issue 或向我们提交 Pull Request。

# SemVer 版本规范

本项目使用语义化版本号规范 (SemVer) 来管理版本。格式为 MAJOR.MINOR.PATCH。

* MAJOR 版本号表示不向后兼容的重大更改。
* MINOR 版本号表示向后兼容的新功能或增强。
* PATCH 版本号表示向后兼容的错误修复和维护。
  有关详细信息，请参阅 [语义化版本](https://semver.org/lang/zh-CN/) 规范。

## 参考

* [Agora API 文档](https://doc.shengwang.cn/)

## 许可证

该项目使用MIT许可证，详细信息请参阅LICENSE文件。
