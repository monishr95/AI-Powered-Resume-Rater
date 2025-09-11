package com.example.resumerater.controller;

import com.example.resumerater.model.ComparisonResult;
import com.example.resumerater.model.ResumeEntity;
import com.example.resumerater.model.User;
import com.example.resumerater.repository.ComparisonRepository;
import com.example.resumerater.repository.ResumeRepository;
import com.example.resumerater.repository.UserRepository;
import com.example.resumerater.service.LlmService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compare")
@CrossOrigin(origins = "http://localhost:3000")
public class CompareController {


    @Autowired
    private ComparisonRepository comparisonRepository;
    private final LlmService llmService;
    private final ResumeRepository resumeRepo;
    private final ComparisonRepository compRepo;
    private final UserRepository userRepo;
    private final ObjectMapper om = new ObjectMapper();

    public CompareController(LlmService llmService, ResumeRepository resumeRepo,
                             ComparisonRepository compRepo, UserRepository userRepo) {
        this.llmService = llmService;
        this.resumeRepo = resumeRepo;
        this.compRepo = compRepo;
        this.userRepo = userRepo;
    }

    @PostMapping("")
    public ResponseEntity<?> compare(@RequestBody Map<String, Object> body) {
        try {
            Long userId = Long.valueOf(String.valueOf(body.get("userId")));
            String jobDescription = (String) body.get("jobDescription");
            String resumeText = (String) body.getOrDefault("resumeText", null);

            if (resumeText == null && body.containsKey("resumeId")) {
                Long rid = Long.valueOf(String.valueOf(body.get("resumeId")));
                resumeText = resumeRepo.findById(rid).map(ResumeEntity::getContent).orElse("");
            }

            String modelResponse = llmService.analyzeResumeVsJob(resumeText, jobDescription);

            // parse modelResponse (should be JSON)
            JsonNode analysis = om.readTree(modelResponse);
            double score = analysis.path("match_score").asDouble();
            String matchedSkillsJson = om.writeValueAsString(analysis.path("matched_skills"));
            String missingSkillsJson = om.writeValueAsString(analysis.path("missing_skills"));
            String suggestions = analysis.path("suggestions").asText();

            ComparisonResult cr = new ComparisonResult();
            User u = userRepo.findById(userId).orElse(null);
            cr.setUser(u);
            if (body.containsKey("resumeId")) {
                Long rid = Long.valueOf(String.valueOf(body.get("resumeId")));
                resumeRepo.findById(rid).ifPresent(cr::setResume);
            }
            cr.setJobDescription(jobDescription);
            cr.setMatchScore(score);
            cr.setMatchedSkillsJson(matchedSkillsJson);
            cr.setMissingSkillsJson(missingSkillsJson);
            cr.setSuggestions(suggestions);
            cr.setCreatedAt(Instant.now());

            compRepo.save(cr);

            Map<String, Object> resp = Map.of(
                    "matchScore", score,
                    "matchedSkills", om.readTree(matchedSkillsJson),
                    "missingSkills", om.readTree(missingSkillsJson),
                    "suggestions", suggestions
            );

            return ResponseEntity.ok(resp);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("error", ex.getMessage()));
        }
    }
    
    @GetMapping("/user/{userId}")
    public List<ComparisonResult> getUserComparisons(@PathVariable Long userId) {
        return comparisonRepository.findByUserId(userId);
    }
}
