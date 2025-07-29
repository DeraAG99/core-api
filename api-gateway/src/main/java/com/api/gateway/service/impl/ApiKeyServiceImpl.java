package com.api.gateway.service.impl;

import com.api.gateway.model.dto.internal.ApiKeyValidationResult;
import com.api.gateway.model.entity.ApiKeyEntity;
import com.api.gateway.model.entity.RateLimitEntity;
import com.api.gateway.repository.ApiKeyRepository;
import com.api.gateway.repository.IpWhiteListRepository;
import com.api.gateway.repository.RateLimitRepository;
import com.api.gateway.service.ApiKeyService;
import com.api.gateway.util.ApiKeyUtil;
import com.api.gateway.util.ApiKeyValidatorUtil;
import com.api.gateway.util.ScopeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {
    private final ApiKeyRepository apiKeyRepository;
    private final IpWhiteListRepository ipWhitelistRepository;
    private final RateLimitRepository rateLimitRepository;

    @Override
    public Mono<ApiKeyEntity> findByKeyHash(String keyHash) {
        return apiKeyRepository.findByKeyHashAndStatus(keyHash, "ACTIVE");
    }

    @Override
    public Mono<Boolean> isIpWhitelisted(UUID apiKeyId, String ipAddress) {
        return ipWhitelistRepository.isIpAllowed(apiKeyId, ipAddress);
    }

    @Override
    public Mono<Tuple2<ApiKeyEntity, RateLimitEntity>> findKeyWithRateLimit(String apiKeyHash) {
        return apiKeyRepository.findByKeyHash(apiKeyHash)
                .flatMap(apiKey -> {
                    if (apiKey.getRateLimitId() == null) {
                        return Mono.just(Tuples.of(apiKey, null));
                    }
                    return rateLimitRepository.findById(apiKey.getRateLimitId())
                            .defaultIfEmpty(null)
                            .map(rateLimit -> Tuples.of(apiKey, rateLimit));
                });
    }

    @Override
    public Mono<RateLimitEntity> getRateLimitConfig(UUID rateLimitId) {
        return rateLimitRepository.findById(rateLimitId);
    }

    @Override
    public Mono<ApiKeyValidationResult> validateAccess(String rawApiKey, String ip, String requiredScope) {
        String hash = ApiKeyUtil.hashApiKey(rawApiKey);

        return findByKeyHash(hash)
                .flatMap(apiKey -> {
                    if (ApiKeyValidatorUtil.isExpired(apiKey.getExpiresAt())) {
                        return Mono.just(new ApiKeyValidationResult(apiKey.getId(), null, false, 401,requiredScope));
                    }

                    return isIpWhitelisted(apiKey.getId(), ip)
                            .flatMap(allowed -> {
                                if (!allowed) {
                                    return Mono.just(new ApiKeyValidationResult(apiKey.getId(), null, false, 403, requiredScope));
                                }

                                if (!ScopeUtil.hasScope(apiKey.getScopes(), requiredScope)) {
                                    return Mono.just(new ApiKeyValidationResult(apiKey.getId(), null, false, 403, requiredScope));
                                }

                                return Mono.just(new ApiKeyValidationResult(
                                        apiKey.getId(),
                                        apiKey.getRateLimitId(),
                                        true,
                                        200,
                                        requiredScope

                                ));
                            });
                });
    }
}
