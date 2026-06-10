package com.obsyl.ingestion.api.dto;

import com.obsyl.ingestion.domain.LogEvent;

public record LogIngestionResponse(
        String status,
        String message,
        String level,
        String service,
        String timestamp,
        String traceId
) {

    public static LogIngestionResponse accepted(LogEvent event) {
        return new LogIngestionResponse(
                "accepted",
                event.getMessage(),
                event.getLevel(),
                event.getService(),
                event.getTimestamp().toString(),
                event.getTraceId()
        );
    }
}
