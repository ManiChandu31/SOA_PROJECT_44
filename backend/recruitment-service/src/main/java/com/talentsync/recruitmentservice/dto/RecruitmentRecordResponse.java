package com.talentsync.recruitmentservice.dto;

import com.talentsync.recruitmentservice.entity.RecruitmentRecord;
import com.talentsync.recruitmentservice.entity.RecruitmentStage;
import java.time.LocalDateTime;

public class RecruitmentRecordResponse {

    private Long id;
    private Long applicationId;
    private Long candidateId;
    private Long jobId;
    private RecruitmentStage currentStage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RecruitmentRecordResponse() {
    }

    public RecruitmentRecordResponse(RecruitmentRecord record) {
        this.id = record.getId();
        this.applicationId = record.getApplicationId();
        this.candidateId = record.getCandidateId();
        this.jobId = record.getJobId();
        this.currentStage = record.getCurrentStage();
        this.createdAt = record.getCreatedAt();
        this.updatedAt = record.getUpdatedAt();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public RecruitmentStage getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(RecruitmentStage currentStage) {
        this.currentStage = currentStage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
