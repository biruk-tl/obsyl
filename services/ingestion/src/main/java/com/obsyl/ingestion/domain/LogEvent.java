package com.obsyl.ingestion.domain;

import java.time.Instant;
import java.util.Objects;

/**
 * Canonical internal representation of an ingested log event.
 * Framework-free by design to keep domain logic portable.
 */
public final class LogEvent {

    private final String message;
    private final String level;
    private final String service;
    private final Instant timestamp;
    private final String traceId;

    public LogEvent(String message, String level, String service, Instant timestamp, String traceId) {
        this.message = Objects.requireNonNull(message, "message must not be null");
        this.level = Objects.requireNonNull(level, "level must not be null");
        this.service = Objects.requireNonNull(service, "service must not be null");
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp must not be null");
        this.traceId = traceId;
    }

    public String getMessage() {
        return message;
    }

    public String getLevel() {
        return level;
    }

    public String getService() {
        return service;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getTraceId() {
        return traceId;
    }
}
