package com.obsyl.ingestion.domain;

import java.util.Objects;

/**
 * Log-specific payload for a {@link TelemetryEvent}.
 * Framework-free value object aligned with telemetry-event-v1.
 */
public final class LogEvent {

    private final String level;
    private final String message;

    public LogEvent(String level, String message) {
        this.level = Objects.requireNonNull(level, "level must not be null");
        this.message = Objects.requireNonNull(message, "message must not be null");
    }

    public String getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }
}
