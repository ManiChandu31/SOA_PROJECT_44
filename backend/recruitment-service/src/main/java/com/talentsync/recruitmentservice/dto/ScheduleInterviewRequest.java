package com.talentsync.recruitmentservice.dto;

import com.talentsync.recruitmentservice.entity.InterviewMode;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ScheduleInterviewRequest {

    @NotNull(message = "Application ID is required")
    private Long applicationId;

    @NotBlank(message = "Interview round is required")
    @Size(max = 50, message = "Round name must not exceed 50 characters")
    private String round;

    @NotNull(message = "Scheduled date and time is required")
    @Future(message = "Interview schedule must be in the future")
    private LocalDateTime scheduledDateTime;

    @NotNull(message = "Interview mode is required")
    private InterviewMode interviewMode;

    private String meetingLink;
    private String location;

    @NotBlank(message = "Interviewer name is required")
    @Size(max = 100, message = "Interviewer name must not exceed 100 characters")
    private String interviewerName;

    public ScheduleInterviewRequest() {
    }

    public ScheduleInterviewRequest(Long applicationId, String round, LocalDateTime scheduledDateTime,
                                    InterviewMode interviewMode, String meetingLink, String location,
                                    String interviewerName) {
        this.applicationId = applicationId;
        this.round = round;
        this.scheduledDateTime = scheduledDateTime;
        this.interviewMode = interviewMode;
        this.meetingLink = meetingLink;
        this.location = location;
        this.interviewerName = interviewerName;
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
}
