package com.example.resumerater.controller;

import com.example.resumerater.model.User;
import com.example.resumerater.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) { this.userService = userService; }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "email and password required"));
        }
        User saved = userService.register(user);
        saved.setPassword(null);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            Map<String, Object> payload = userService.login(body.get("email"), body.get("password"));
            return ResponseEntity.ok(payload);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(401).body(Map.of("error", ex.getMessage()));
        }
    }
}
