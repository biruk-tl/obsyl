package com.obsyl.ingestion.application;

/**
 * Raised when an incoming log ingestion request fails application-layer validation.
 */
public class InvalidLogRequestException extends RuntimeException {

    public InvalidLogRequestException(String message) {
        super(message);
    }
}
