package io.agora.rest.services.cloudrecording.api;

import java.time.Duration;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.agora.rest.core.Context;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

public abstract class BaseAPI {
    protected final Context context;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final Integer maxAttempts;

    protected final String pathPrefix;

    public BaseAPI(Context context, String pathPrefix, Integer maxAttempts) {
        this.context = context;
        this.pathPrefix = pathPrefix;
        this.maxAttempts = maxAttempts;
    }

    protected RetryBackoffSpec customRetry(Predicate<Throwable> retryPredicate) {
        return Retry.backoff(maxAttempts, Duration.ofSeconds(1)) // Maximum 5 retry attempts, initial 1-second interval
                .maxBackoff(Duration.ofSeconds(5)) // Maximum 5-second interval
                .filter(retryPredicate) // Retry condition
                .doBeforeRetry(retrySignal -> {
                    long retryCount = retrySignal.totalRetries() + 1; // Current retry attempt number
                    Duration nextBackoff = Duration.ofSeconds(retryCount); // Next retry interval
                    logger.warn("Retry attempt: {}, next backoff: {}", retryCount, nextBackoff);
                }).onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    logger.error("Retry exhausted: {}", retrySignal.totalRetries());
                    return retrySignal.failure();
                });
    }
}
