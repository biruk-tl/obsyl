package com.obsyl.ingestion;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class IngestionApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void ingestLogReturnsTelemetryEventWhenRequiredFieldsPresent() throws Exception {
		mockMvc.perform(post("/ingest/log")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "service": "obsyl-ingestion",
								  "level": "INFO",
								  "message": "service started"
								}
								"""))
				.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.eventId").exists())
				.andExpect(jsonPath("$.service").value("obsyl-ingestion"))
				.andExpect(jsonPath("$.environment").value("unknown"))
				.andExpect(jsonPath("$.schemaVersion").value("v1"))
				.andExpect(jsonPath("$.eventType").value("LOG"))
				.andExpect(jsonPath("$.logEvent.level").value("INFO"))
				.andExpect(jsonPath("$.logEvent.message").value("service started"))
				.andExpect(jsonPath("$.timestamp").exists());
	}

	@Test
	void ingestLogRejectedWhenRequiredFieldsMissing() throws Exception {
		mockMvc.perform(post("/ingest/log")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "message": "service started"
								}
								"""))
				.andExpect(status().isBadRequest());
	}
}
