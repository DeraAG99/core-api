package com.api.gateway.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Table("api_keys")
public class ApiKeyEntity {

    @Id
    private UUID id;

    @Column("key_hash")
    private String keyHash;

    @Column("vendor_name")
    private String vendorName;

    private String status;

    @Column("scopes")
    private List<String> scopes;// nanti bisa pakai converter biar jadi List<String>

    @Column("rate_limit_id")
    private UUID rateLimitId;

    @Column("expires_at")
    private LocalDateTime expiresAt;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}