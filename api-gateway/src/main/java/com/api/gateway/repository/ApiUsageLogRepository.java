package com.api.gateway.repository;

import com.api.gateway.model.entity.ApiUsageLogEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ApiUsageLogRepository extends ReactiveCrudRepository<ApiUsageLogEntity, UUID> {
    // Bisa ditambah method custom kalau perlu logging filter spesifik
}
