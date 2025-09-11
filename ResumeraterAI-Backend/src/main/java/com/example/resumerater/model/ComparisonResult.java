package com.example.resumerater.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "comparisons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComparisonResult {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private ResumeEntity resume;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String jobDescription;

    private Double matchScore;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String matchedSkillsJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String missingSkillsJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String suggestions;

    private Instant createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ResumeEntity getResume() {
		return resume;
	}

	public void setResume(ResumeEntity resume) {
		this.resume = resume;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public Double getMatchScore() {
		return matchScore;
	}

	public void setMatchScore(Double matchScore) {
		this.matchScore = matchScore;
	}

	public String getMatchedSkillsJson() {
		return matchedSkillsJson;
	}

	public void setMatchedSkillsJson(String matchedSkillsJson) {
		this.matchedSkillsJson = matchedSkillsJson;
	}

	public String getMissingSkillsJson() {
		return missingSkillsJson;
	}

	public void setMissingSkillsJson(String missingSkillsJson) {
		this.missingSkillsJson = missingSkillsJson;
	}

	public String getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(String suggestions) {
		this.suggestions = suggestions;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public ComparisonResult(Long id, User user, ResumeEntity resume, String jobDescription, Double matchScore,
			String matchedSkillsJson, String missingSkillsJson, String suggestions, Instant createdAt) {
		super();
		this.id = id;
		this.user = user;
		this.resume = resume;
		this.jobDescription = jobDescription;
		this.matchScore = matchScore;
		this.matchedSkillsJson = matchedSkillsJson;
		this.missingSkillsJson = missingSkillsJson;
		this.suggestions = suggestions;
		this.createdAt = createdAt;
	}

	public ComparisonResult() {
		super();
	}
    
    
}
