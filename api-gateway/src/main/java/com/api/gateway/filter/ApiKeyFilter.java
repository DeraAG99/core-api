package com.api.gateway.filter;

import com.api.gateway.service.ApiKeyService;
import com.api.gateway.service.ApiUsageLogService;
import com.api.gateway.service.RateLimitService;
import com.api.gateway.util.AccessLogUtil;
import com.api.gateway.util.RequestUtil;
import com.api.gateway.util.ResponseUtil;
import com.api.gateway.util.ScopeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class ApiKeyFilter implements WebFilter, Ordered {

    private final ApiKeyService apiKeyService;
    private final ApiUsageLogService apiUsageLogService;
    private final RateLimitService rateLimitService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var request = exchange.getRequest();
        var headers = request.getHeaders();

        String rawApiKey = headers.getFirst("X-API-Key");
        String ip = RequestUtil.getClientIp(exchange);
        String path = request.getPath().toString();

        // ➕ Bypass ping endpoint (dan bisa kamu tambah list lain juga)
        if (path.equals("/ping")) {
            return chain.filter(exchange);
        }

        if (rawApiKey == null || rawApiKey.isEmpty()) {
            return ResponseUtil.unauthorized(exchange);
        }

        String requiredScope = ScopeUtil.getScopeFromRequest(request);

        return apiKeyService.validateAccess(rawApiKey, ip, requiredScope)
                .flatMap(result -> {
                    if (!result.valid()) {
                        return AccessLogUtil.logAndBlock(apiUsageLogService,result.apiKeyId(), path, ip, result.status(), exchange);
                    }

                    if (result.rateLimitId() == null) {
                        ServerWebExchange mutatedExchange = exchange.mutate()
                                .request(exchange.getRequest()
                                        .mutate()
                                        .header("X-Api-Key-Id", result.apiKeyId().toString())
                                        .header("X-Client-Ip", ip)
                                        .header("X-Scopes", result.scope())
                                        .build())
                                .build();
                        return AccessLogUtil.logAndAllow(apiUsageLogService,result.apiKeyId(), path, ip, exchange, chain);
                    }

                    return rateLimitService.isAllowed(result.apiKeyId(), result.rateLimitId())
                            .flatMap(allowed -> {
                                if (!allowed) {
                                    return AccessLogUtil.logAndBlock(apiUsageLogService, result.apiKeyId(), path, ip, 429, exchange);
                                }
                                // ✅ inject headers after rate limit check
                                ServerWebExchange mutatedExchange = exchange.mutate()
                                        .request(exchange.getRequest()
                                                .mutate()
                                                .header("X-Api-Key-Id", result.apiKeyId().toString())
                                                .header("X-Client-Ip", ip)
                                                .header("X-Scopes", result.scope())
                                                .build())
                                        .build();
                                return AccessLogUtil.logAndAllow(apiUsageLogService,result.apiKeyId(), path, ip, exchange, chain);
                            });
                })
                .switchIfEmpty(AccessLogUtil.logAndBlock(apiUsageLogService,null, path, ip, 401, exchange));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}