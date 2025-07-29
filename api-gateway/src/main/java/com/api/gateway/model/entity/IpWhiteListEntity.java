package com.api.gateway.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table("ip_whitelist")
public class IpWhiteListEntity {
    @Id
    private UUID id;

    @Column("ip_address")
    private String ipAddress;

    @Column("api_key_id")
    private UUID apiKeyId;

    private String description;

    @Column("is_active")
    private boolean isActive;

    @Column("is_global")
    private boolean isGlobal;

    @Column("created_at")
    private LocalDateTime createdAt;
}
