package com.api.gateway.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class ResponseUtil {
    public static Mono<Void> setStatus(ServerWebExchange exchange, int statusCode) {
        exchange.getResponse().setStatusCode(HttpStatus.valueOf(statusCode));
        return exchange.getResponse().setComplete();
    }

    public static Mono<Void> unauthorized(ServerWebExchange exchange) {
        return setStatus(exchange, HttpStatus.UNAUTHORIZED.value());
    }

    public static Mono<Void> forbidden(ServerWebExchange exchange) {
        return setStatus(exchange, HttpStatus.FORBIDDEN.value());
    }

    public static Mono<Void> tooManyRequests(ServerWebExchange exchange) {
        return setStatus(exchange, HttpStatus.TOO_MANY_REQUESTS.value());
    }
}
