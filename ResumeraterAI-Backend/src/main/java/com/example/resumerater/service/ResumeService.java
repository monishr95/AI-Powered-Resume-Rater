package com.example.resumerater.service;

import com.example.resumerater.model.ResumeEntity;
import com.example.resumerater.model.User;
import com.example.resumerater.repository.ResumeRepository;
import com.example.resumerater.repository.UserRepository;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepo;
    private final UserRepository userRepo;
    private final Tika tika = new Tika();

    public ResumeService(ResumeRepository resumeRepo, UserRepository userRepo) {
        this.resumeRepo = resumeRepo;
        this.userRepo = userRepo;
    }

    public ResumeEntity storeResume(MultipartFile file, Long userId) throws Exception {
        String text = tika.parseToString(file.getInputStream());
        ResumeEntity r = new ResumeEntity();
        r.setFilename(file.getOriginalFilename());
        r.setContent(text);
        r.setUploadedAt(Instant.now());
        User u = userRepo.findById(userId).orElse(null);
        r.setUser(u);
        return resumeRepo.save(r);
    }

    public List<ResumeEntity> listByUser(Long userId) {
        return resumeRepo.findByUserId(userId);
    }

    public Optional<ResumeEntity> findById(Long id) {
        return resumeRepo.findById(id);
    }
}
