package com.talentsync.recruitmentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class OfferDecisionRequest {

    @NotBlank(message = "Decision is required")
    @Pattern(regexp = "^(ACCEPT|DECLINE)$", message = "Decision must be either ACCEPT or DECLINE")
    private String decision;

    public OfferDecisionRequest() {
    }

    public OfferDecisionRequest(String decision) {
        this.decision = decision;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
