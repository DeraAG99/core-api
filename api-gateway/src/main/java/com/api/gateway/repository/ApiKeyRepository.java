package com.api.gateway.repository;

import com.api.gateway.model.entity.ApiKeyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ApiKeyRepository extends ReactiveCrudRepository<ApiKeyEntity, UUID> {
    Mono<ApiKeyEntity> findByKeyHash(String keyHash);
    // Cari API Key by key_hash dan status aktif
    Mono<ApiKeyEntity> findByKeyHashAndStatus(String keyHash, String status);
}
