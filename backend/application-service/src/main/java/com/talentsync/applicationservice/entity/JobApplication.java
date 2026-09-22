package com.talentsync.applicationservice.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name="applications", uniqueConstraints=@UniqueConstraint(columnNames={"candidateId","jobId"}))
public class JobApplication {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private Long candidateId; @Column(nullable=false) private Long jobId;
 @Column(nullable=false) private String resumeFileName; @Column(nullable=false) private String resumeStoragePath;
 @Column(columnDefinition="TEXT") private String coverLetter; @Enumerated(EnumType.STRING) @Column(nullable=false) private ApplicationStatus status=ApplicationStatus.APPLIED;
 @Column(nullable=false,updatable=false) private Instant appliedAt; @Column(nullable=false) private Instant updatedAt;
 @PrePersist void create(){appliedAt=Instant.now();updatedAt=appliedAt;} @PreUpdate void update(){updatedAt=Instant.now();}
 public Long getId(){return id;} public Long getCandidateId(){return candidateId;} public void setCandidateId(Long v){candidateId=v;} public Long getJobId(){return jobId;} public void setJobId(Long v){jobId=v;} public String getResumeFileName(){return resumeFileName;} public void setResumeFileName(String v){resumeFileName=v;} public String getResumeStoragePath(){return resumeStoragePath;} public void setResumeStoragePath(String v){resumeStoragePath=v;} public String getCoverLetter(){return coverLetter;} public void setCoverLetter(String v){coverLetter=v;} public ApplicationStatus getStatus(){return status;} public void setStatus(ApplicationStatus v){status=v;} public Instant getAppliedAt(){return appliedAt;} public Instant getUpdatedAt(){return updatedAt;}
}
