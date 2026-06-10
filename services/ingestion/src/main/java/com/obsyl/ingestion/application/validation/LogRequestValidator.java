package com.obsyl.ingestion.application.validation;

import com.obsyl.ingestion.api.dto.LogRequest;
import com.obsyl.ingestion.api.error.InvalidLogRequestException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class LogRequestValidator {

    private static final Set<String> ALLOWED_LEVELS = Set.of("INFO", "WARN", "ERROR", "DEBUG");

    public void validate(LogRequest request) {
        requirePresent(request.service(), "service");
        requirePresent(request.message(), "message");
        requireLevel(request.level());
    }

    private void requirePresent(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidLogRequestException(fieldName + " is required");
        }
    }

    private void requireLevel(String level) {
        if (level == null) {
            throw new InvalidLogRequestException("level is required");
        }

        String normalizedLevel = level.trim().toUpperCase();
        if (!ALLOWED_LEVELS.contains(normalizedLevel)) {
            throw new InvalidLogRequestException("level must be one of: INFO, WARN, ERROR, DEBUG");
        }
    }
}
