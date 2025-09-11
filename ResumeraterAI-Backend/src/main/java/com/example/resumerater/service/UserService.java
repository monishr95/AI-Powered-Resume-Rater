package com.example.resumerater.service;

import com.example.resumerater.model.User;
import com.example.resumerater.repository.UserRepository;
import com.example.resumerater.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepo, BCryptPasswordEncoder encoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    public User register(User u) {
        u.setPassword(encoder.encode(u.getPassword()));
        return userRepo.save(u);
    }

    public Map<String, Object> login(String email, String rawPassword) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid credentials"));
        if (!encoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(user.getEmail(), user.getId());
        user.setPassword(null);
        Map<String, Object> res = new HashMap<>();
        res.put("token", token);
        res.put("user", user);
        return res;
    }
}
