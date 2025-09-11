package com.example.resumerater.repository;

import com.example.resumerater.model.ResumeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<ResumeEntity, Long> {
    List<ResumeEntity> findByUserId(Long userId);
}
