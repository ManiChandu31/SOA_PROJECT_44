package com.talentsync.recruitmentservice.dto;

import com.talentsync.recruitmentservice.entity.InterviewMode;
import com.talentsync.recruitmentservice.entity.InterviewStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class UpdateInterviewRequest {

    private String round;
    private LocalDateTime scheduledDateTime;
    private InterviewMode interviewMode;
    private String meetingLink;
    private String location;
    private String interviewerName;
    private InterviewStatus status;

    public UpdateInterviewRequest() {
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
}
