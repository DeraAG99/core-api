package com.api.gateway.util;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;
import java.util.Locale;

public class ScopeUtil {

    /**
     * Mendapatkan scope berdasarkan path & HTTP method.
     * Format: {service}.{resource}.{method} — contoh: secure.user.GET
     */
    public static String getScopeFromPath(String path, String method) {
        String[] segments = path.split("/");

        if (segments.length < 2) {
            return ""; // path tidak valid, misal: "/"
        }

        String service = segments[1]; // ambil segmen pertama setelah "/"
        if (service.isBlank()) return "";

        String resource = getResourcePart(segments);
        String action = method.toUpperCase(Locale.ROOT);

        return String.format("%s.%s.%s", service, resource, action);
    }

    /**
     * Mendapatkan scope berdasarkan ServerHttpRequest (untuk digunakan langsung di filter)
     */
    public static String getScopeFromRequest(ServerHttpRequest request) {
        String path = request.getPath().value();
        String method = request.getMethod().toString();
        return getScopeFromPath(path, method);
    }

    /**
     * Menggabungkan sisa path menjadi resource. Misal: ["secure", "user", "info"] → "user-info"
     */
    private static String getResourcePart(String[] segments) {
        if (segments.length < 3) return "root"; // hanya "/secure" saja
        StringBuilder builder = new StringBuilder();
        for (int i = 2; i < segments.length; i++) {
            if (!segments[i].isBlank()) {
                if (builder.length() > 0) builder.append("-");
                builder.append(segments[i]);
            }
        }
        return builder.toString();
    }

    public static boolean hasScope(List<String> scopes, String requiredScope) {
        return scopes != null && scopes.contains(requiredScope);
    }
}