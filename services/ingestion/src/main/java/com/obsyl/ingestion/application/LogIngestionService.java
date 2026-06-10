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

@Service
public class LogIngestionService {

    private static final String SCHEMA_VERSION = "1";
    private static final String DEFAULT_ENVIRONMENT = "local";

    public TelemetryEvent ingest(LogRequest request) {
        Instant timestamp = parseOrNow(request.timestamp());
        LogEvent logEvent = new LogEvent(
                normalizeLevel(request.level()),
                normalizeMessage(request.message())
        );

        TelemetryEvent event = new TelemetryEvent(
                UUID.randomUUID().toString(),
                timestamp,
                normalizeService(request.service()),
                DEFAULT_ENVIRONMENT,
                SCHEMA_VERSION,
                EventType.LOG,
                buildMetadata(request.traceId()),
                logEvent,
                null,
                null
        );

        // Future extension point:
        // - publish event to Kafka for asynchronous distributed processing
        // - persist event into durable storage (PostgreSQL / Elasticsearch)
        return event;
    }

    private Map<String, Object> buildMetadata(String traceId) {
        String normalizedTraceId = normalizeTraceId(traceId);
        if (normalizedTraceId == null) {
            return Map.of();
        }
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("traceId", normalizedTraceId);
        return Map.copyOf(metadata);
    }

    private Instant parseOrNow(String timestamp) {
        if (timestamp == null || timestamp.isBlank()) {
            return Instant.now();
        }

        try {
            return Instant.parse(timestamp);
        } catch (DateTimeParseException ignored) {
            return Instant.now();
        }
    }

    private String normalizeMessage(String message) {
        if (message == null || message.isBlank()) {
            return "<empty-log-message>";
        }
        return message.trim();
    }

    private String normalizeLevel(String level) {
        if (level == null || level.isBlank()) {
            return "INFO";
        }
        return level.trim().toUpperCase();
    }

    private String normalizeService(String service) {
        if (service == null || service.isBlank()) {
            return "unknown-service";
        }
        return service.trim();
    }

    private String normalizeTraceId(String traceId) {
        if (traceId == null || traceId.isBlank()) {
            return null;
        }
        return traceId.trim();
    }
}
