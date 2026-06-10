package com.obsyl.ingestion.api.error;

import java.time.Instant;

/**
 * Standardized API error response for ingestion failures.
 */
public record ErrorResponse(
        String errorCode,
        String message,
        Instant timestamp
) {
}
