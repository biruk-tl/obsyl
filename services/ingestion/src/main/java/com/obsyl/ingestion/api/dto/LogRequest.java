package com.obsyl.ingestion.api.dto;

import java.time.Instant;

/**
 * External HTTP payload for log ingestion requests.
 * API-boundary only — must not be used outside the API layer.
 */
public record LogRequest(
        String service,
        String level,
        String message,
        Instant timestamp,
        String environment
) {
}
