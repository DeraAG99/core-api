package com.api.gateway.util;

import java.time.LocalDateTime;

public class ApiKeyValidatorUtil {

    public static boolean isExpired(LocalDateTime expiresAt) {
        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }

}