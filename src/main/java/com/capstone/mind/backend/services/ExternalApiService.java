package com.capstone.mind.backend.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExternalApiService {

    private static final Logger log = LoggerFactory.getLogger(ExternalApiService.class);

    @CircuitBreaker(name = "externalService", fallbackMethod = "fallback")
    @RateLimiter(name = "externalService")
    public String callExternalApi(boolean fail) {
        log.info("[ExternalApiService] Calling external API | fail={}", fail);

        if (fail) {
            log.error("[ExternalApiService] Simulated failure");
            throw new RuntimeException("External service is down!");
        }

        return "External API response at " + System.currentTimeMillis();
    }

    // fallback method must match original signature + Throwable
    public String fallback(boolean fail, Throwable t) {
        log.warn("[ExternalApiService] Fallback triggered due to: {}", t.toString());
        return "Fallback response due to error: " + t.getClass().getSimpleName();
    }
}
