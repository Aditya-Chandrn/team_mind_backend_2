package com.capstone.mind.backend.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class MockExternalAuthService {
    private static final Logger logger = LoggerFactory.getLogger(MockExternalAuthService.class);

    @RateLimiter(name = "login")
    @CircuitBreaker(name = "login", fallbackMethod = "authenticateFallback")
    public boolean authenticate(String email, String password) {
        // Simulate external API call that might fail
        double random = ThreadLocalRandom.current().nextDouble();
        if (random < 0.3) {
            throw new RuntimeException("External authentication service unavailable");
        }
        return true;
    }

    public boolean authenticateFallback(String email, String password, Throwable t) {
        logger.warn("External auth service failed, using fallback", t);
        return true; // Proceed with login despite external service failure
    }
}