package com.obsyl.ingestion.application;

import com.obsyl.ingestion.api.dto.LogRequest;
import com.obsyl.ingestion.application.validation.LogRequestValidator;
import com.obsyl.ingestion.domain.EventType;
import com.obsyl.ingestion.domain.LogEvent;
import com.obsyl.ingestion.domain.TelemetryEvent;
import com.obsyl.ingestion.domain.TelemetryEventEnvelope;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Core use-case handler for log ingestion.
 * Validates requests and converts them into enveloped domain telemetry events.
 */
@Service
public class LogIngestionService {

    private static final String SCHEMA_VERSION = "v1";
    private static final String DEFAULT_ENVIRONMENT = "unknown";

    private final LogRequestValidator logRequestValidator;

    public LogIngestionService(LogRequestValidator logRequestValidator) {
        this.logRequestValidator = logRequestValidator;
    }

    public TelemetryEventEnvelope ingest(LogRequest request) {
        logRequestValidator.validate(request);

        Instant timestamp = request.timestamp() != null ? request.timestamp() : Instant.now();
        String environment = resolveEnvironment(request.environment());
        LogEvent logEvent = new LogEvent(
                request.level().trim().toUpperCase(),
                request.message().trim()
        );

        TelemetryEvent event = new TelemetryEvent(
                UUID.randomUUID().toString(),
                timestamp,
                request.service().trim(),
                environment,
                SCHEMA_VERSION,
                EventType.LOG,
                Map.of(),
                logEvent,
                null,
                null
        );

        return TelemetryEventEnvelope.wrap(event);
    }

    private String resolveEnvironment(String environment) {
        if (environment == null || environment.isBlank()) {
            return DEFAULT_ENVIRONMENT;
        }
        return environment.trim();
    }
}
