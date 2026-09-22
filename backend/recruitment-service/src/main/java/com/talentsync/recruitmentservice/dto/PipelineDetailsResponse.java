package com.talentsync.recruitmentservice.dto;

import com.talentsync.recruitmentservice.entity.RecruitmentStage;
import java.time.LocalDateTime;
import java.util.List;

public class PipelineDetailsResponse {

    private Long recordId;
    private Long applicationId;
    private Long candidateId;
    private Long jobId;
    private RecruitmentStage currentStage;
    private List<InterviewResponse> interviews;
    private OfferResponse offer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PipelineDetailsResponse() {
    }

    public PipelineDetailsResponse(Long recordId, Long applicationId, Long candidateId, Long jobId,
                                   RecruitmentStage currentStage, List<InterviewResponse> interviews,
                                   OfferResponse offer, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.recordId = recordId;
        this.applicationId = applicationId;
        this.candidateId = candidateId;
        this.jobId = jobId;
        this.currentStage = currentStage;
        this.interviews = interviews;
        this.offer = offer;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
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

    public List<InterviewResponse> getInterviews() {
        return interviews;
    }

    public void setInterviews(List<InterviewResponse> interviews) {
        this.interviews = interviews;
    }

    public OfferResponse getOffer() {
        return offer;
    }

    public void setOffer(OfferResponse offer) {
        this.offer = offer;
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
