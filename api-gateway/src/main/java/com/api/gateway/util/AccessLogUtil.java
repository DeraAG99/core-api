package com.api.gateway.util;

import com.api.gateway.service.ApiUsageLogService;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;


public class AccessLogUtil {

    public static Mono<Void> logAndBlock(ApiUsageLogService logService, UUID apiKeyId, String path, String ip, int status, ServerWebExchange exchange) {
        return logService.logAccess(apiKeyId, path, ip, status)
                .then(ResponseUtil.setStatus(exchange, status));
    }

    public static Mono<Void> logAndAllow(ApiUsageLogService logService, UUID apiKeyId, String path, String ip, ServerWebExchange exchange, WebFilterChain chain) {
        return logService.logAccess(apiKeyId, path, ip, 200)
                .then(chain.filter(exchange));
    }

}
