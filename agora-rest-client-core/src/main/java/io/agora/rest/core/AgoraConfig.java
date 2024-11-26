package io.agora.rest.core;

public class AgoraConfig {
    private final String appId;

    private final Credential credential;

    private final RegionArea regionArea;

    private final HttpProperty httpProperty;

    private AgoraConfig(Builder builder) {
        this.appId = builder.appId;
        this.credential = builder.credential;
        this.regionArea = builder.regionArea;
        this.httpProperty = builder.httpProperty;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getAppId() {
        return appId;
    }

    public Credential getCredential() {
        return credential;
    }

    public RegionArea getRegionArea() {
        return regionArea;
    }

    public HttpProperty getHttpProperty() {
        return httpProperty;
    }

    @Override
    public String toString() {
        return "AgoraConfig{" +
                "appId='" + appId + '\'' +
                ", credential=" + credential +
                ", regionArea=" + regionArea +
                ", httpProperty=" + httpProperty +
                '}';
    }

    public static class Builder {

        private String appId;

        private Credential credential;

        private RegionArea regionArea;

        private HttpProperty httpProperty;

        private Builder() {
        }

        public Builder appId(String appId) {
            this.appId = appId;
            return this;
        }

        public Builder credential(Credential credential) {
            this.credential = credential;
            return this;
        }

        public Builder regionArea(RegionArea regionArea) {
            this.regionArea = regionArea;
            return this;
        }

        private Builder httpProperty(HttpProperty httpProperty) {
            this.httpProperty = httpProperty;
            return this;
        }

        public AgoraConfig build() {
            if (httpProperty == null) {
                this.httpProperty = HttpProperty.builder().build();
            }
            return new AgoraConfig(this);
        }
    }
}
