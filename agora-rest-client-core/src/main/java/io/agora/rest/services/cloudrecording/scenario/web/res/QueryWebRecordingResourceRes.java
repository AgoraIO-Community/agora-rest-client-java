package io.agora.rest.services.cloudrecording.scenario.web.res;

import io.agora.rest.services.cloudrecording.api.res.QueryResourceRes;

/**
 * @brief Response returned by the web recording query API.
 * @since v0.4.0
 */
public class QueryWebRecordingResourceRes {
    /**
     * The name of the channel to be recorded.
     */
    private String cname;

    /**
     * The user ID used by the cloud recording service in the RTC channel to
     * identify the recording service in the channel.
     */
    private String uid;

    /**
     * Unique identifier of the resource.
     */
    private String resourceId;

    /**
     * Unique identifier of the recording session.
     */
    private String sid;

    /**
     * Server response. See {@link QueryResourceRes.WebRecordingServerResponse}.
     */
    private QueryResourceRes.WebRecordingServerResponse serverResponse;

    public static Builder builder() {
        return new Builder();
    }

    private QueryWebRecordingResourceRes(Builder builder) {
        setCname(builder.cname);
        setUid(builder.uid);
        setResourceId(builder.resourceId);
        setSid(builder.sid);
        setServerResponse(builder.serverResponse);
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

    public QueryResourceRes.WebRecordingServerResponse getServerResponse() {
        return serverResponse;
    }

    public void setServerResponse(QueryResourceRes.WebRecordingServerResponse serverResponse) {
        this.serverResponse = serverResponse;
    }

    @Override
    public String toString() {
        return "QueryWebRecordingResourceRes{" +
                "cname='" + cname + '\'' +
                ", uid='" + uid + '\'' +
                ", resourceId='" + resourceId + '\'' +
                ", sid='" + sid + '\'' +
                ", serverResponse=" + serverResponse +
                '}';
    }

    public static final class Builder {
        private String cname;
        private String uid;
        private String resourceId;
        private String sid;
        private QueryResourceRes.WebRecordingServerResponse serverResponse;

        private Builder() {
        }

        public Builder cname(String val) {
            cname = val;
            return this;
        }

        public Builder uid(String val) {
            uid = val;
            return this;
        }

        public Builder resourceId(String val) {
            resourceId = val;
            return this;
        }

        public Builder sid(String val) {
            sid = val;
            return this;
        }

        public Builder serverResponse(QueryResourceRes.WebRecordingServerResponse val) {
            serverResponse = val;
            return this;
        }

        public QueryWebRecordingResourceRes build() {
            return new QueryWebRecordingResourceRes(this);
        }
    }
}
