package com.obsyl.ingestion.api.dto;

/**
 * External HTTP payload for log ingestion requests.
 * This DTO belongs to the API boundary and should not leak into domain internals.
 */
public record LogRequest(
        String message,
        String level,
        String service,
        String timestamp,
        String environment,
        String traceId
) {
}
