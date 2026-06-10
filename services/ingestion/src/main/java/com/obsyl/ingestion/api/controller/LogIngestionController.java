package com.obsyl.ingestion.api.controller;

import com.obsyl.ingestion.api.dto.LogIngestionResponse;
import com.obsyl.ingestion.api.dto.LogRequest;
import com.obsyl.ingestion.application.LogIngestionService;
import com.obsyl.ingestion.domain.LogEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ingest")
public class LogIngestionController {

    private final LogIngestionService logIngestionService;

    public LogIngestionController(LogIngestionService logIngestionService) {
        this.logIngestionService = logIngestionService;
    }

    @PostMapping("/log")
    public ResponseEntity<LogIngestionResponse> ingestLog(@RequestBody LogRequest request) {
        LogEvent event = logIngestionService.ingest(request);
        return ResponseEntity.accepted().body(LogIngestionResponse.accepted(event));
    }
}
