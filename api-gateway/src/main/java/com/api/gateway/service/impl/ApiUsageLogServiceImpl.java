package com.api.gateway.service.impl;

import com.api.gateway.model.entity.ApiUsageLogEntity;
import com.api.gateway.repository.ApiUsageLogRepository;
import com.api.gateway.service.ApiUsageLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiUsageLogServiceImpl implements ApiUsageLogService {

    private final ApiUsageLogRepository apiUsageLogRepository;

    @Override
    public Mono<Void> logAccess(UUID apiKeyId, String path, String ip, int statusCode) {
        ApiUsageLogEntity logEntity = new ApiUsageLogEntity();
        logEntity.setApiKeyId(apiKeyId);
        logEntity.setPath(path);
        logEntity.setIpAddress(ip);
        logEntity.setStatusCode(statusCode);
        logEntity.setTimestamp(LocalDateTime.now());

        Mono<Void> saveToDb = apiUsageLogRepository.save(logEntity).then();

        log.info("[API_ACCESS] apiKeyId={}, ip={}, path={}, status={}",
                apiKeyId, ip, path, statusCode);

        return saveToDb;
    }


}