package com.talentsync.recruitmentservice.dto;

import jakarta.validation.constraints.NotBlank;

public class InterviewFeedbackRequest {

    @NotBlank(message = "Interview feedback is required")
    private String feedback;

    private boolean markCompleted = true;

    public InterviewFeedbackRequest() {
    }

    public InterviewFeedbackRequest(String feedback, boolean markCompleted) {
        this.feedback = feedback;
        this.markCompleted = markCompleted;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public boolean isMarkCompleted() {
        return markCompleted;
    }

    public void setMarkCompleted(boolean markCompleted) {
        this.markCompleted = markCompleted;
    }
}
