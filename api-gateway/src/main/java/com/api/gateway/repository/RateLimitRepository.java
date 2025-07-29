package com.api.gateway.repository;


import com.api.gateway.model.entity.RateLimitEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface RateLimitRepository extends ReactiveCrudRepository<RateLimitEntity, UUID> {
}