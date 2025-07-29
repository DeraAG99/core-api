package com.api.gateway.util;

import org.springframework.web.server.ServerWebExchange;

public class RequestUtil {
    public static String getClientIp(ServerWebExchange exchange) {
        String ip = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (ip == null && exchange.getRequest().getRemoteAddress() != null) {
            ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        }
        return ip != null ? ip : "unknown";
    }
}
