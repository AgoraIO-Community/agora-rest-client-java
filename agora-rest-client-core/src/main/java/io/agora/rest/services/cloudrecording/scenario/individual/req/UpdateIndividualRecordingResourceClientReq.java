package io.agora.rest.services.cloudrecording.scenario.individual.req;

import io.agora.rest.services.cloudrecording.api.req.UpdateResourceReq;

public class UpdateIndividualRecordingResourceClientReq {

    private UpdateResourceReq.StreamSubscribe streamSubscribe;

    public static Builder builder() {
        return new Builder();
    }

    private UpdateIndividualRecordingResourceClientReq(Builder builder) {
        setStreamSubscribe(builder.streamSubscribe);
    }

    public UpdateResourceReq.StreamSubscribe getStreamSubscribe() {
        return streamSubscribe;
    }

    public void setStreamSubscribe(UpdateResourceReq.StreamSubscribe streamSubscribe) {
        this.streamSubscribe = streamSubscribe;
    }

    @Override
    public String toString() {
        return "UpdateIndividualRecordingResourceClientReq{" +
                "streamSubscribe=" + streamSubscribe +
                '}';
    }

    public static final class Builder {
        private UpdateResourceReq.StreamSubscribe streamSubscribe;

        private Builder() {
        }

        public Builder streamSubscribe(UpdateResourceReq.StreamSubscribe val) {
            streamSubscribe = val;
            return this;
        }

        public UpdateIndividualRecordingResourceClientReq build() {
            return new UpdateIndividualRecordingResourceClientReq(this);
        }
    }
}