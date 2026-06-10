package com.obsyl.ingestion.application;

import java.time.Instant;

/**
 * Application-layer input for the log ingestion use case.
 * Keeps the API DTO boundary out of application logic.
 */
public record IngestLogCommand(
        String service,
        String level,
        String message,
        Instant timestamp,
        String environment
) {
}
