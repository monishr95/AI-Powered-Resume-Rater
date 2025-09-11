package com.example.resumerater.controller;

import com.example.resumerater.model.ResumeEntity;
import com.example.resumerater.repository.UserRepository;
import com.example.resumerater.service.ResumeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resumes")
@CrossOrigin(origins = "http://localhost:3000")
public class ResumeController {

    private final ResumeService resumeService;
    private final UserRepository userRepo;

    public ResumeController(ResumeService resumeService, UserRepository userRepo) {
        this.resumeService = resumeService;
        this.userRepo = userRepo;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId) {
        try {
            ResumeEntity saved = resumeService.storeResume(file, userId);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> listByUser(@PathVariable Long userId) {
        List<ResumeEntity> list = resumeService.listByUser(userId);
        return ResponseEntity.ok(list);
    }
}
