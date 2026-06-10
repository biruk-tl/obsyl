package com.obsyl.ingestion.api.error;

/**
 * Raised when an incoming log ingestion request fails validation.
 */
public class InvalidLogRequestException extends RuntimeException {

    public InvalidLogRequestException(String message) {
        super(message);
    }
}
