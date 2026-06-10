package com.obsyl.ingestion.application;

import com.obsyl.ingestion.api.dto.LogRequest;
import com.obsyl.ingestion.application.validation.LogRequestValidator;
import com.obsyl.ingestion.domain.EventType;
import com.obsyl.ingestion.domain.LogEvent;
import com.obsyl.ingestion.domain.TelemetryEvent;
import com.obsyl.ingestion.domain.TelemetryEventEnvelope;
import com.obsyl.ingestion.domain.schema.TelemetrySchemaVersion;
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

    private static final String DEFAULT_ENVIRONMENT = "unknown";

    private final LogRequestValidator logRequestValidator;

    public LogIngestionService(LogRequestValidator logRequestValidator) {
        this.logRequestValidator = logRequestValidator;
    }

    public TelemetryEventEnvelope ingest(LogRequest request) {
        logRequestValidator.validate(request);

        String incomingSchemaVersion = resolveIncomingSchemaVersion(request.schemaVersion());
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
                TelemetrySchemaVersion.V1,
                EventType.LOG,
                Map.of(),
                logEvent,
                null,
                null
        );

        // Normalize all incoming events to internal V1 format.
        // Future support for V2 transformation layer:
        // if (TelemetrySchemaVersion.V2.equals(incomingSchemaVersion)) {
        //     event = telemetrySchemaEvolutionStrategy.transformV2ToV1(event);
        // }
        TelemetryEvent normalizedEvent = normalizeToV1(event, incomingSchemaVersion);

        return TelemetryEventEnvelope.wrap(normalizedEvent);
    }

    private String resolveIncomingSchemaVersion(String schemaVersion) {
        if (schemaVersion == null || schemaVersion.isBlank()) {
            return TelemetrySchemaVersion.V1;
        }
        return schemaVersion.trim();
    }

    private TelemetryEvent normalizeToV1(TelemetryEvent event, String incomingSchemaVersion) {
        if (TelemetrySchemaVersion.V2.equals(incomingSchemaVersion)) {
            // Future: return telemetrySchemaEvolutionStrategy.transformV2ToV1(event);
        }
        return event;
    }

    private String resolveEnvironment(String environment) {
        if (environment == null || environment.isBlank()) {
            return DEFAULT_ENVIRONMENT;
        }
        return environment.trim();
    }
}
