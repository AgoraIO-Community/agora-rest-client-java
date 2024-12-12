package io.agora.rest.services.cloudrecording.api.res;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.agora.rest.exception.AgoraJsonException;
import io.agora.rest.services.cloudrecording.enums.CloudRecordingModeEnum;

import java.util.List;

public class QueryResourceRes {
    @JsonProperty("cname")
    private String cname;

    @JsonProperty("uid")
    private String uid;

    @JsonProperty("resourceId")
    private String resourceId;

    @JsonProperty("sid")
    private String sid;

    @JsonProperty("serverResponse")
    private JsonNode serverResponse;

    @JsonIgnore
    private ServerResponseType serverResponseType;

    @JsonIgnore
    private QueryIndividualRecordingServerResponse queryIndividualRecordingServerResponse;

    @JsonIgnore
    private QueryIndividualVideoScreenshotServerResponse queryIndividualVideoScreenshotServerResponse;

    @JsonIgnore
    private MixRecordingHLSServerResponse mixRecordingHLSServerResponse;

    @JsonIgnore
    private MixRecordingHLSAndMP4ServerResponse mixRecordingHLSAndMP4ServerResponse;

    @JsonIgnore
    private WebRecordingServerResponse webRecordingServerResponse;

    @JsonIgnore
    public void setServerResponse(CloudRecordingModeEnum mode) throws Exception {
        JsonNode fileListModeNode = this.serverResponse.path("fileListMode");
        this.serverResponseType = ServerResponseType.QUERY_SERVER_RESPONSE_UNKNOWN_TYPE;

        ObjectMapper objectMapper = new ObjectMapper();

        switch (mode) {
            case INDIVIDUAL:
                if (fileListModeNode.isTextual() && "json".equals(fileListModeNode.asText())) {
                    this.serverResponseType = ServerResponseType.QUERY_INDIVIDUAL_RECORDING_SERVER_RESPONSE_TYPE;
                    this.queryIndividualRecordingServerResponse = objectMapper.treeToValue(this.serverResponse,
                            QueryIndividualRecordingServerResponse.class);
                } else {
                    this.serverResponseType = ServerResponseType.QUERY_INDIVIDUAL_VIDEO_SCREENSHOT_SERVER_RESPONSE_TYPE;
                    this.queryIndividualVideoScreenshotServerResponse = objectMapper.treeToValue(this.serverResponse,
                            QueryIndividualVideoScreenshotServerResponse.class);
                }
                break;
            case MIX:
                if (fileListModeNode.isMissingNode()) {
                    break;
                }

                if ("string".equals(fileListModeNode.asText())) {
                    this.serverResponseType = ServerResponseType.QUERY_MIX_RECORDING_HLS_SERVER_RESPONSE_TYPE;
                    this.mixRecordingHLSServerResponse = objectMapper.treeToValue(this.serverResponse,
                            MixRecordingHLSServerResponse.class);
                } else if ("json".equals(fileListModeNode.asText())) {
                    this.serverResponseType = ServerResponseType.QUERY_MIX_RECORDING_HLS_AND_MP4_SERVER_RESPONSE_TYPE;
                    this.mixRecordingHLSAndMP4ServerResponse = objectMapper.treeToValue(this.serverResponse,
                            MixRecordingHLSAndMP4ServerResponse.class);
                } else {
                    throw new AgoraJsonException("unknown fileList mode");
                }
                break;
            case WEB:
                this.serverResponseType = ServerResponseType.QUERY_WEB_RECORDING_SERVER_RESPONSE_TYPE;
                this.webRecordingServerResponse = objectMapper.treeToValue(this.serverResponse,
                        WebRecordingServerResponse.class);
                break;
        }

    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public JsonNode getServerResponse() {
        return serverResponse;
    }

    public void setServerResponse(JsonNode serverResponse) {
        this.serverResponse = serverResponse;
    }

    public ServerResponseType getServerResponseType() {
        return serverResponseType;
    }

    public void setServerResponseType(ServerResponseType serverResponseType) {
        this.serverResponseType = serverResponseType;
    }

    public QueryIndividualRecordingServerResponse getQueryIndividualRecordingServerResponse() {
        return queryIndividualRecordingServerResponse;
    }

    public void setQueryIndividualRecordingServerResponse(QueryIndividualRecordingServerResponse queryIndividualRecordingServerResponse) {
        this.queryIndividualRecordingServerResponse = queryIndividualRecordingServerResponse;
    }

    public QueryIndividualVideoScreenshotServerResponse getQueryIndividualVideoScreenshotServerResponse() {
        return queryIndividualVideoScreenshotServerResponse;
    }

    public void setQueryIndividualVideoScreenshotServerResponse(QueryIndividualVideoScreenshotServerResponse queryIndividualVideoScreenshotServerResponse) {
        this.queryIndividualVideoScreenshotServerResponse = queryIndividualVideoScreenshotServerResponse;
    }

    public MixRecordingHLSServerResponse getMixRecordingHLSServerResponse() {
        return mixRecordingHLSServerResponse;
    }

    public void setMixRecordingHLSServerResponse(MixRecordingHLSServerResponse mixRecordingHLSServerResponse) {
        this.mixRecordingHLSServerResponse = mixRecordingHLSServerResponse;
    }

    public MixRecordingHLSAndMP4ServerResponse getMixRecordingHLSAndMP4ServerResponse() {
        return mixRecordingHLSAndMP4ServerResponse;
    }

    public void setMixRecordingHLSAndMP4ServerResponse(MixRecordingHLSAndMP4ServerResponse mixRecordingHLSAndMP4ServerResponse) {
        this.mixRecordingHLSAndMP4ServerResponse = mixRecordingHLSAndMP4ServerResponse;
    }

    public WebRecordingServerResponse getWebRecordingServerResponse() {
        return webRecordingServerResponse;
    }

    public void setWebRecordingServerResponse(WebRecordingServerResponse webRecordingServerResponse) {
        this.webRecordingServerResponse = webRecordingServerResponse;
    }

    @Override
    public String toString() {
        return "QueryResourceRes{" +
                "webRecordingServerResponse=" + webRecordingServerResponse +
                ", mixRecordingHLSAndMP4ServerResponse=" + mixRecordingHLSAndMP4ServerResponse +
                ", mixRecordingHLSServerResponse=" + mixRecordingHLSServerResponse +
                ", queryIndividualVideoScreenshotServerResponse=" + queryIndividualVideoScreenshotServerResponse +
                ", queryIndividualRecordingServerResponse=" + queryIndividualRecordingServerResponse +
                ", serverResponseType=" + serverResponseType +
                ", serverResponse=" + serverResponse +
                ", sid='" + sid + '\'' +
                ", resourceId='" + resourceId + '\'' +
                ", uid='" + uid + '\'' +
                ", cname='" + cname + '\'' +
                '}';
    }

    public enum ServerResponseType {
        QUERY_SERVER_RESPONSE_UNKNOWN_TYPE,
        QUERY_INDIVIDUAL_RECORDING_SERVER_RESPONSE_TYPE,
        QUERY_INDIVIDUAL_VIDEO_SCREENSHOT_SERVER_RESPONSE_TYPE,
        QUERY_MIX_RECORDING_HLS_SERVER_RESPONSE_TYPE,
        QUERY_MIX_RECORDING_HLS_AND_MP4_SERVER_RESPONSE_TYPE,
        QUERY_WEB_RECORDING_SERVER_RESPONSE_TYPE
    }

    public static class QueryIndividualRecordingServerResponse {
        @JsonProperty("status")
        private Integer status;

        @JsonProperty("fileListMode")
        private String fileListMode;

        @JsonProperty("fileList")
        private List<FileDetail> fileList;

        @JsonProperty("sliceStartTime")
        private Long sliceStartTime;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getFileListMode() {
            return fileListMode;
        }

        public void setFileListMode(String fileListMode) {
            this.fileListMode = fileListMode;
        }

        public List<FileDetail> getFileList() {
            return fileList;
        }

        public void setFileList(List<FileDetail> fileList) {
            this.fileList = fileList;
        }

        public Long getSliceStartTime() {
            return sliceStartTime;
        }

        public void setSliceStartTime(Long sliceStartTime) {
            this.sliceStartTime = sliceStartTime;
        }

        @Override
        public String toString() {
            return "QueryIndividualRecordingServerResponse{" +
                    "status=" + status +
                    ", fileListMode='" + fileListMode + '\'' +
                    ", fileList=" + fileList +
                    ", sliceStartTime=" + sliceStartTime +
                    '}';
        }

        public static class FileDetail {
            @JsonProperty("fileName")
            private String fileName;

            @JsonProperty("trackType")
            private String trackType;

            @JsonProperty("uid")
            private String uid;

            @JsonProperty("mixedAllUser")
            private Boolean mixedAllUser;

            @JsonProperty("isPlayable")
            private Boolean isPlayable;

            @JsonProperty("sliceStartTime")
            private Long sliceStartTime;

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            public String getTrackType() {
                return trackType;
            }

            public void setTrackType(String trackType) {
                this.trackType = trackType;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public Boolean getMixedAllUser() {
                return mixedAllUser;
            }

            public void setMixedAllUser(Boolean mixedAllUser) {
                this.mixedAllUser = mixedAllUser;
            }

            public Boolean getPlayable() {
                return isPlayable;
            }

            public void setPlayable(Boolean playable) {
                isPlayable = playable;
            }

            public Long getSliceStartTime() {
                return sliceStartTime;
            }

            public void setSliceStartTime(Long sliceStartTime) {
                this.sliceStartTime = sliceStartTime;
            }

            @Override
            public String toString() {
                return "FileDetail{" +
                        "fileName='" + fileName + '\'' +
                        ", trackType='" + trackType + '\'' +
                        ", uid='" + uid + '\'' +
                        ", mixedAllUser=" + mixedAllUser +
                        ", isPlayable=" + isPlayable +
                        ", sliceStartTime=" + sliceStartTime +
                        '}';
            }
        }
    }

    public static class QueryIndividualVideoScreenshotServerResponse {
        @JsonProperty("status")
        private Integer status;

        @JsonProperty("sliceStartTime")
        private Long sliceStartTime;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Long getSliceStartTime() {
            return sliceStartTime;
        }

        public void setSliceStartTime(Long sliceStartTime) {
            this.sliceStartTime = sliceStartTime;
        }

        @Override
        public String toString() {
            return "QueryIndividualVideoScreenshotServerResponse{" +
                    "status=" + status +
                    ", sliceStartTime=" + sliceStartTime +
                    '}';
        }
    }

    public static class MixRecordingHLSServerResponse {
        @JsonProperty("status")
        private Integer status;

        @JsonProperty("fileListMode")
        private String fileListMode;

        @JsonProperty("fileList")
        private String fileList;

        @JsonProperty("sliceStartTime")
        private Long sliceStartTime;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getFileListMode() {
            return fileListMode;
        }

        public void setFileListMode(String fileListMode) {
            this.fileListMode = fileListMode;
        }

        public String getFileList() {
            return fileList;
        }

        public void setFileList(String fileList) {
            this.fileList = fileList;
        }

        public Long getSliceStartTime() {
            return sliceStartTime;
        }

        public void setSliceStartTime(Long sliceStartTime) {
            this.sliceStartTime = sliceStartTime;
        }

        @Override
        public String toString() {
            return "MixRecordingHLSServerResponse{" +
                    "status=" + status +
                    ", fileListMode='" + fileListMode + '\'' +
                    ", fileList='" + fileList + '\'' +
                    ", sliceStartTime=" + sliceStartTime +
                    '}';
        }
    }

    public static class MixRecordingHLSAndMP4ServerResponse {
        @JsonProperty("status")
        private Integer status;

        @JsonProperty("fileListMode")
        private String fileListMode;

        @JsonProperty("fileList")
        private List<FileDetail> fileList;

        @JsonProperty("sliceStartTime")
        private Long sliceStartTime;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getFileListMode() {
            return fileListMode;
        }

        public void setFileListMode(String fileListMode) {
            this.fileListMode = fileListMode;
        }

        public List<FileDetail> getFileList() {
            return fileList;
        }

        public void setFileList(List<FileDetail> fileList) {
            this.fileList = fileList;
        }

        public Long getSliceStartTime() {
            return sliceStartTime;
        }

        public void setSliceStartTime(Long sliceStartTime) {
            this.sliceStartTime = sliceStartTime;
        }

        @Override
        public String toString() {
            return "MixRecordingHLSAndMP4ServerResponse{" +
                    "status=" + status +
                    ", fileListMode='" + fileListMode + '\'' +
                    ", fileList=" + fileList +
                    ", sliceStartTime=" + sliceStartTime +
                    '}';
        }

        public static class FileDetail {
            @JsonProperty("fileName")
            private String fileName;

            @JsonProperty("trackType")
            private String trackType;

            @JsonProperty("uid")
            private String uid;

            @JsonProperty("mixedAllUser")
            private Boolean mixedAllUser;

            @JsonProperty("isPlayable")
            private Boolean isPlayable;

            @JsonProperty("sliceStartTime")
            private Long sliceStartTime;

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            public String getTrackType() {
                return trackType;
            }

            public void setTrackType(String trackType) {
                this.trackType = trackType;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public Boolean getMixedAllUser() {
                return mixedAllUser;
            }

            public void setMixedAllUser(Boolean mixedAllUser) {
                this.mixedAllUser = mixedAllUser;
            }

            public Boolean getPlayable() {
                return isPlayable;
            }

            public void setPlayable(Boolean playable) {
                isPlayable = playable;
            }

            public Long getSliceStartTime() {
                return sliceStartTime;
            }

            public void setSliceStartTime(Long sliceStartTime) {
                this.sliceStartTime = sliceStartTime;
            }

            @Override
            public String toString() {
                return "FileDetail{" +
                        "fileName='" + fileName + '\'' +
                        ", trackType='" + trackType + '\'' +
                        ", uid='" + uid + '\'' +
                        ", mixedAllUser=" + mixedAllUser +
                        ", isPlayable=" + isPlayable +
                        ", sliceStartTime=" + sliceStartTime +
                        '}';
            }
        }
    }

    public static class WebRecordingServerResponse {
        @JsonProperty("status")
        private Integer status;

        @JsonProperty("extensionServiceState")
        private List<ExtensionServiceState> extensionServiceState;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public List<ExtensionServiceState> getExtensionServiceState() {
            return extensionServiceState;
        }

        public void setExtensionServiceState(List<ExtensionServiceState> extensionServiceState) {
            this.extensionServiceState = extensionServiceState;
        }

        @Override
        public String toString() {
            return "WebRecordingServerResponse{" +
                    "status=" + status +
                    ", extensionServiceState=" + extensionServiceState +
                    '}';
        }

        public static class ExtensionServiceState {
            @JsonProperty("payload")
            private Payload payload;

            @JsonProperty("serviceName")
            private String serviceName;

            @Override
            public String toString() {
                return "ExtensionServiceState{" +
                        "payload=" + payload +
                        ", serviceName='" + serviceName + '\'' +
                        '}';
            }

            public static class Payload {
                @JsonProperty("fileList")
                private List<FileDetail> fileList;

                @JsonProperty("onhold")
                private Boolean onhold;

                @JsonProperty("state")
                private String state;

                @JsonProperty("outputs")
                private List<Output> outputs;

                public List<FileDetail> getFileList() {
                    return fileList;
                }

                public void setFileList(List<FileDetail> fileList) {
                    this.fileList = fileList;
                }

                public Boolean getOnhold() {
                    return onhold;
                }

                public void setOnhold(Boolean onhold) {
                    this.onhold = onhold;
                }

                public String getState() {
                    return state;
                }

                public void setState(String state) {
                    this.state = state;
                }

                public List<Output> getOutputs() {
                    return outputs;
                }

                public void setOutputs(List<Output> outputs) {
                    this.outputs = outputs;
                }

                @Override
                public String toString() {
                    return "Payload{" +
                            "fileList=" + fileList +
                            ", onhold=" + onhold +
                            ", state='" + state + '\'' +
                            ", outputs=" + outputs +
                            '}';
                }

                public static class FileDetail {
                    @JsonProperty("filename")
                    private String filename;

                    @JsonProperty("sliceStartTime")
                    private Long sliceStartTime;

                    public String getFilename() {
                        return filename;
                    }

                    public void setFilename(String filename) {
                        this.filename = filename;
                    }

                    public Long getSliceStartTime() {
                        return sliceStartTime;
                    }

                    public void setSliceStartTime(Long sliceStartTime) {
                        this.sliceStartTime = sliceStartTime;
                    }

                    @Override
                    public String toString() {
                        return "FileDetail{" +
                                "filename='" + filename + '\'' +
                                ", sliceStartTime=" + sliceStartTime +
                                '}';
                    }
                }

                public static class Output {
                    @JsonProperty("rtmpUrl")
                    private String rtmpUrl;

                    @JsonProperty("status")
                    private String status;

                    public String getRtmpUrl() {
                        return rtmpUrl;
                    }

                    public void setRtmpUrl(String rtmpUrl) {
                        this.rtmpUrl = rtmpUrl;
                    }

                    public String getStatus() {
                        return status;
                    }

                    public void setStatus(String status) {
                        this.status = status;
                    }

                    @Override
                    public String toString() {
                        return "Output{" +
                                "rtmpUrl='" + rtmpUrl + '\'' +
                                ", status='" + status + '\'' +
                                '}';
                    }
                }
            }
        }
    }
}
