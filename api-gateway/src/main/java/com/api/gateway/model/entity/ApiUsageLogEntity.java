package com.api.gateway.model.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table("api_usage_logs")
public class ApiUsageLogEntity {

    @Id
    private UUID id;

    @Column("api_key_id")
    private UUID apiKeyId;

    private String path;

    @Column("ip_address")
    private String ipAddress;

    @Column("status_code")
    private int statusCode;

    private LocalDateTime timestamp;
}