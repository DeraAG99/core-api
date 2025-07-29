package com.api.gateway.model.entity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Table("rate_limits")
public class RateLimitEntity {
    @Id
    private UUID id;
    @Column("name")
    private String name;
    @Column("limit_count")
    private int limitCount;
    @Column("per_seconds")
    private int perSeconds;
    @Column("created_at")
    private LocalDateTime createdAt;
}