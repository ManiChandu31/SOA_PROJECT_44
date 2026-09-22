package com.talentsync.recruitmentservice.dto;

import com.talentsync.recruitmentservice.entity.RecruitmentStage;
import jakarta.validation.constraints.NotNull;

public class CreateRecruitmentRecordRequest {

    @NotNull(message = "Application ID is required")
    private Long applicationId;

    @NotNull(message = "Candidate ID is required")
    private Long candidateId;

    @NotNull(message = "Job ID is required")
    private Long jobId;

    private RecruitmentStage initialStage = RecruitmentStage.APPLIED;

    public CreateRecruitmentRecordRequest() {
    }

    public CreateRecruitmentRecordRequest(Long applicationId, Long candidateId, Long jobId) {
        this.applicationId = applicationId;
        this.candidateId = candidateId;
        this.jobId = jobId;
        this.initialStage = RecruitmentStage.APPLIED;
    }

    public CreateRecruitmentRecordRequest(Long applicationId, Long candidateId, Long jobId, RecruitmentStage initialStage) {
        this.applicationId = applicationId;
        this.candidateId = candidateId;
        this.jobId = jobId;
        this.initialStage = initialStage != null ? initialStage : RecruitmentStage.APPLIED;
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

    public RecruitmentStage getInitialStage() {
        return initialStage;
    }

    public void setInitialStage(RecruitmentStage initialStage) {
        this.initialStage = initialStage;
    }
}
