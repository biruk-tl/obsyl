package com.obsyl.ingestion.application;

import com.obsyl.ingestion.api.dto.LogRequest;
import com.obsyl.ingestion.domain.LogEvent;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeParseException;

@Service
public class LogIngestionService {

    public LogEvent ingest(LogRequest request) {
        Instant timestamp = parseOrNow(request.timestamp());

        LogEvent event = new LogEvent(
                normalizeMessage(request.message()),
                normalizeLevel(request.level()),
                normalizeService(request.service()),
                timestamp,
                normalizeTraceId(request.traceId())
        );

        // Future extension point:
        // - publish event to Kafka for asynchronous distributed processing
        // - persist event into durable storage (PostgreSQL / Elasticsearch)
        return event;
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
