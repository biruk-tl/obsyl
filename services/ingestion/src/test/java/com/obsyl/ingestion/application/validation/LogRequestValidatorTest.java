package com.obsyl.ingestion.application.validation;

import com.obsyl.ingestion.api.dto.LogRequest;
import com.obsyl.ingestion.api.error.InvalidLogRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogRequestValidatorTest {

    private final LogRequestValidator validator = new LogRequestValidator();

    @Test
    void validateAcceptsValidRequest() {
        var request = new LogRequest("obsyl-ingestion", "INFO", "service started", null, null);

        assertDoesNotThrow(() -> validator.validate(request));
    }

    @Test
    void validateAcceptsCaseInsensitiveLevel() {
        var request = new LogRequest("obsyl-ingestion", "warn", "service started", null, null);

        assertDoesNotThrow(() -> validator.validate(request));
    }

    @Test
    void validateRejectsMissingService() {
        var request = new LogRequest(null, "INFO", "service started", null, null);

        var exception = assertThrows(InvalidLogRequestException.class, () -> validator.validate(request));

        assertTrue(exception.getMessage().contains("service"));
    }

    @Test
    void validateRejectsInvalidLevel() {
        var request = new LogRequest("obsyl-ingestion", "CRITICAL", "service started", null, null);

        var exception = assertThrows(InvalidLogRequestException.class, () -> validator.validate(request));

        assertTrue(exception.getMessage().contains("level must be one of"));
    }
}
