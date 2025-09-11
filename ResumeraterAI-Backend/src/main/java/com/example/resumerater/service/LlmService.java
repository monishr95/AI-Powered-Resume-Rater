package com.example.resumerater.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class LlmService {

    private final WebClient webClient;
    private final String model;
    private final ObjectMapper om = new ObjectMapper();

    public LlmService(@Value("${openrouter.api.key}") String key,
                      @Value("${openrouter.api.url}") String url,
                      @Value("${openrouter.model}") String model) {
        this.model = model;
        this.webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + key)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String analyzeResumeVsJob(String resumeText, String jobDescription) {
        String prompt = "You are an expert ATS/resume analyzer. Compare the resume and job description below.\n"
                + "Return a JSON object with keys: match_score (number 0-100), matched_skills (array), missing_skills (array), suggestions (string).\n"
                + "Respond only with valid JSON and nothing else.\n\nResume:\n" + resumeText + "\n\nJob Description:\n" + jobDescription;

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "temperature", 0
        );

        String resp = webClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block(Duration.ofSeconds(30));

        // extract choices[0].message.content
        try {
            JsonNode root = om.readTree(resp);
            JsonNode content = root.path("choices").get(0).path("message").path("content");
            return content.asText();
        } catch (Exception ex) {
            throw new RuntimeException("LLM parse error: " + ex.getMessage() + " | raw: " + resp);
        }
    }
}
