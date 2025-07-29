package com.api.gateway.service;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RateLimitService {
    Mono<Boolean> isAllowed(UUID apiKeyId, int limit, int perSeconds);
    Mono<Boolean> isAllowed(UUID apiKeyId, UUID rateLimitId);
}
