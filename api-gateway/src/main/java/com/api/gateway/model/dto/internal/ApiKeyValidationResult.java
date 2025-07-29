package com.api.gateway.model.dto.internal;


import java.util.UUID;

public record ApiKeyValidationResult(
        UUID apiKeyId,
        UUID rateLimitId,
        boolean valid,
        int status,
        String scope
) {}

