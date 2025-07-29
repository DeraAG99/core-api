package com.api.gateway.repository;

import com.api.gateway.model.entity.IpWhiteListEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface IpWhiteListRepository extends ReactiveCrudRepository<IpWhiteListEntity, UUID> {

    // Cek apakah IP di whitelist untuk API Key tertentu
    Flux<IpWhiteListEntity> findByApiKeyIdAndIsActive(UUID apiKeyId, boolean isActive);

    // Ambil global whitelist IP aktif
    Flux<IpWhiteListEntity> findByIsGlobalAndIsActive(boolean isGlobal, boolean isActive);
    @Query("""
        SELECT EXISTS (
            SELECT 1 FROM ip_whitelist
            WHERE api_key_id = :apiKeyId
              AND ip_address = :ipAddress
              AND is_active = true
        )
    """)
    Mono<Boolean> isIpAllowed(UUID apiKeyId, String ipAddress);
}
