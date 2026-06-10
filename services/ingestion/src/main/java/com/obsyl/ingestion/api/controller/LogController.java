package com.obsyl.ingestion.api.controller;

import com.obsyl.ingestion.api.dto.IngestionResponse;
import com.obsyl.ingestion.api.dto.LogRequest;
import com.obsyl.ingestion.application.LogIngestionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ingest")
public class LogController {

    private final LogIngestionService logIngestionService;

    public LogController(LogIngestionService logIngestionService) {
        this.logIngestionService = logIngestionService;
    }

    @PostMapping("/log")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public IngestionResponse ingestLog(@RequestBody LogRequest request) {
        var envelope = logIngestionService.ingest(request);
        return IngestionResponse.success(envelope);
    }
}
