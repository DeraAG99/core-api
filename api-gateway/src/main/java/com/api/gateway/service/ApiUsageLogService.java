package com.api.gateway.service;

import reactor.core.publisher.Mono;
import java.util.UUID;

public interface ApiUsageLogService  {
    Mono<Void> logAccess(UUID apiKeyId, String path, String ip, int statusCode);
}
