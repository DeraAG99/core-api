package com.api.gateway.service.impl;

import com.api.gateway.service.ApiKeyService;
import com.api.gateway.service.RateLimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RateLimitServiceImpl implements RateLimitService {

    private final ReactiveStringRedisTemplate redisTemplate;
    private final ApiKeyService apiKeyService;

    @Override
    public Mono<Boolean> isAllowed(UUID apiKeyId, int limit, int perSeconds) {
        String key = buildKey(String.valueOf(apiKeyId), perSeconds);
        return redisTemplate.opsForValue().increment(key)
                .flatMap(count -> {
                    if (count == 1) {
                        // Set TTL only on first increment
                        return redisTemplate.expire(key, Duration.ofSeconds(perSeconds))
                                .thenReturn(true);
                    }
                    return Mono.just(count <= limit);
                });
    }

    private String buildKey(String apiKeyId, int perSeconds) {
        long currentWindow = System.currentTimeMillis() / (perSeconds * 1000L);
        return "rate_limit:" + apiKeyId + ":" + currentWindow;
    }


    @Override
    public Mono<Boolean> isAllowed(UUID apiKeyId, UUID rateLimitId) {
        return apiKeyService.getRateLimitConfig(rateLimitId)
                .flatMap(limit -> isAllowed(
                        apiKeyId,
                        limit.getLimitCount(),
                        limit.getPerSeconds()
                ))
                .switchIfEmpty(Mono.just(false));
    }
}
