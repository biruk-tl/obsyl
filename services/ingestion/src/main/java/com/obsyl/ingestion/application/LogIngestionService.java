package com.obsyl.ingestion.application;

import com.obsyl.ingestion.api.dto.LogRequest;
import com.obsyl.ingestion.domain.EventType;
import com.obsyl.ingestion.domain.LogEvent;
import com.obsyl.ingestion.domain.TelemetryEvent;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Core use-case handler for log ingestion.
 * Converts validated API requests into canonical domain telemetry events.
 */
@Service
public class LogIngestionService {

    private static final String SCHEMA_VERSION = "v1";
    private static final String DEFAULT_ENVIRONMENT = "unknown";

    public TelemetryEvent ingest(LogRequest request) {
        validate(request);

        Instant timestamp = resolveTimestamp(request.timestamp());
        String environment = resolveEnvironment(request.environment());
        LogEvent logEvent = new LogEvent(
                request.level().trim().toUpperCase(),
                request.message().trim()
        );

        return new TelemetryEvent(
                UUID.randomUUID().toString(),
                timestamp,
                request.service().trim(),
                environment,
                SCHEMA_VERSION,
                EventType.LOG,
                buildMetadata(request.traceId()),
                logEvent,
                null,
                null
        );
    }

    private void validate(LogRequest request) {
        requirePresent(request.service(), "service");
        requirePresent(request.message(), "message");
        requirePresent(request.level(), "level");
    }

    private void requirePresent(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidLogRequestException(fieldName + " is required");
        }
    }

    private Instant resolveTimestamp(String timestamp) {
        if (timestamp == null || timestamp.isBlank()) {
            return Instant.now();
        }

        try {
            return Instant.parse(timestamp.trim());
        } catch (DateTimeParseException ex) {
            throw new InvalidLogRequestException("timestamp must be a valid ISO-8601 instant");
        }
    }

    private String resolveEnvironment(String environment) {
        if (environment == null || environment.isBlank()) {
            return DEFAULT_ENVIRONMENT;
        }
        return environment.trim();
    }

    private Map<String, Object> buildMetadata(String traceId) {
        if (traceId == null || traceId.isBlank()) {
            return Map.of();
        }

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("traceId", traceId.trim());
        return Map.copyOf(metadata);
    }
}
