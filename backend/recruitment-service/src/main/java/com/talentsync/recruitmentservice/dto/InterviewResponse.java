package com.talentsync.recruitmentservice.dto;

import com.talentsync.recruitmentservice.entity.Interview;
import com.talentsync.recruitmentservice.entity.InterviewMode;
import com.talentsync.recruitmentservice.entity.InterviewStatus;

import java.time.LocalDateTime;

public class InterviewResponse {

    private Long id;
    private Long applicationId;
    private String round;
    private LocalDateTime scheduledDateTime;
    private InterviewMode interviewMode;
    private String meetingLink;
    private String location;
    private String interviewerName;
    private InterviewStatus status;
    private String feedback;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public InterviewResponse() {
    }

    public InterviewResponse(Interview interview) {
        this.id = interview.getId();
        this.applicationId = interview.getApplicationId();
        this.round = interview.getRound();
        this.scheduledDateTime = interview.getScheduledDateTime();
        this.interviewMode = interview.getInterviewMode();
        this.meetingLink = interview.getMeetingLink();
        this.location = interview.getLocation();
        this.interviewerName = interview.getInterviewerName();
        this.status = interview.getStatus();
        this.feedback = interview.getFeedback();
        this.createdAt = interview.getCreatedAt();
        this.updatedAt = interview.getUpdatedAt();
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

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public LocalDateTime getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(LocalDateTime scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }

    public InterviewMode getInterviewMode() {
        return interviewMode;
    }

    public void setInterviewMode(InterviewMode interviewMode) {
        this.interviewMode = interviewMode;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInterviewerName() {
        return interviewerName;
    }

    public void setInterviewerName(String interviewerName) {
        this.interviewerName = interviewerName;
    }

    public InterviewStatus getStatus() {
        return status;
    }

    public void setStatus(InterviewStatus status) {
        this.status = status;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
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
