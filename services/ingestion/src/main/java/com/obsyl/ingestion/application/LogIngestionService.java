package com.obsyl.ingestion.application;

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
 * Converts validated application commands into enveloped domain telemetry events.
 */
@Service
public class LogIngestionService {

    private static final String SCHEMA_VERSION = "v1";
    private static final String DEFAULT_ENVIRONMENT = "unknown";

    public TelemetryEventEnvelope ingest(IngestLogCommand command) {
        validate(command);

        Instant timestamp = command.timestamp() != null ? command.timestamp() : Instant.now();
        String environment = resolveEnvironment(command.environment());
        LogEvent logEvent = new LogEvent(
                command.level().trim().toUpperCase(),
                command.message().trim()
        );

        TelemetryEvent event = new TelemetryEvent(
                UUID.randomUUID().toString(),
                timestamp,
                command.service().trim(),
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

    private void validate(IngestLogCommand command) {
        requirePresent(command.service(), "service");
        requirePresent(command.message(), "message");
        requirePresent(command.level(), "level");
    }

    private void requirePresent(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidLogRequestException(fieldName + " is required");
        }
    }

    private String resolveEnvironment(String environment) {
        if (environment == null || environment.isBlank()) {
            return DEFAULT_ENVIRONMENT;
        }
        return environment.trim();
    }
}
