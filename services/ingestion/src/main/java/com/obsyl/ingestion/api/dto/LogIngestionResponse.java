package com.obsyl.ingestion.api.dto;

import com.obsyl.ingestion.domain.TelemetryEvent;

public record LogIngestionResponse(
        String status,
        String message,
        String level,
        String service,
        String timestamp,
        String traceId
) {

    public static LogIngestionResponse accepted(TelemetryEvent event) {
        var logEvent = event.getLogEvent();
        Object traceId = event.getMetadata().get("traceId");

        return new LogIngestionResponse(
                "accepted",
                logEvent.getMessage(),
                logEvent.getLevel(),
                event.getService(),
                event.getTimestamp().toString(),
                traceId == null ? null : traceId.toString()
        );
    }
}
