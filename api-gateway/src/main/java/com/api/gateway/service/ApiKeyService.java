package com.api.gateway.service;

import com.api.gateway.model.dto.internal.ApiKeyValidationResult;
import com.api.gateway.model.entity.ApiKeyEntity;
import com.api.gateway.model.entity.RateLimitEntity;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.UUID;

public interface ApiKeyService {
    Mono<ApiKeyEntity> findByKeyHash(String keyHash);
    Mono<Boolean> isIpWhitelisted(UUID apiKeyId, String ipAddress);
    Mono<Tuple2<ApiKeyEntity, RateLimitEntity>> findKeyWithRateLimit(String apiKeyHash);
    Mono<RateLimitEntity> getRateLimitConfig(UUID rateLimitId);
    Mono<ApiKeyValidationResult> validateAccess(String rawApiKey, String ip, String requiredScope);
}