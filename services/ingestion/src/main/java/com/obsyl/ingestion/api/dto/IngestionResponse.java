package com.obsyl.ingestion.api.dto;

import com.obsyl.ingestion.domain.TelemetryEventEnvelope;

import java.time.Instant;

/**
 * Standardized API response for all ingestion requests.
 */
public record IngestionResponse(
        String eventId,
        String status,
        Instant timestamp,
        String message
) {

    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String LOG_INGESTED_MESSAGE = "log ingested successfully";

    public static IngestionResponse success(TelemetryEventEnvelope envelope) {
        return new IngestionResponse(
                envelope.getEventId(),
                STATUS_SUCCESS,
                Instant.now(),
                LOG_INGESTED_MESSAGE
        );
    }
}
