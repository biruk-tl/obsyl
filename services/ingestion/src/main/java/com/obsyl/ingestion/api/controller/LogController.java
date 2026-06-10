package com.obsyl.ingestion.api.controller;

import com.obsyl.ingestion.api.dto.LogRequest;
import com.obsyl.ingestion.application.IngestLogCommand;
import com.obsyl.ingestion.application.LogIngestionService;
import com.obsyl.ingestion.domain.TelemetryEvent;
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
    public TelemetryEvent ingestLog(@RequestBody LogRequest request) {
        return logIngestionService.ingest(new IngestLogCommand(
                request.service(),
                request.level(),
                request.message(),
                request.timestamp(),
                request.environment()
        ));
    }
}
