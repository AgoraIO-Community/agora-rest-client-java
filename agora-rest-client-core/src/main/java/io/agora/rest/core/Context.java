package io.agora.rest.core;

import io.netty.handler.codec.http.HttpMethod;
import reactor.core.publisher.Mono;

public interface Context {

    <T> Mono<T> sendRequest(String path, HttpMethod method, Object requestBody, Class<T> clazz);

    AgoraConfig getAgoraConfig();
}
