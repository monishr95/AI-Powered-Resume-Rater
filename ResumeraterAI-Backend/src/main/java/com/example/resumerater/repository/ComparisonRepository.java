package com.example.resumerater.repository;

import com.example.resumerater.model.ComparisonResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComparisonRepository extends JpaRepository<ComparisonResult, Long> {
    List<ComparisonResult> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<ComparisonResult> findByUserId(Long userId);
}
