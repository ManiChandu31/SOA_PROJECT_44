package com.talentsync.applicationservice.dto;
import com.talentsync.applicationservice.entity.*; import java.time.Instant;
public record ApplicationResponse(Long id,Long candidateId,Long jobId,String resumeFileName,String coverLetter,ApplicationStatus status,Instant appliedAt,Instant updatedAt){ public static ApplicationResponse from(JobApplication a){return new ApplicationResponse(a.getId(),a.getCandidateId(),a.getJobId(),a.getResumeFileName(),a.getCoverLetter(),a.getStatus(),a.getAppliedAt(),a.getUpdatedAt());} }
