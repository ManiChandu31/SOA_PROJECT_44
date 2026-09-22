package com.talentsync.recruitmentservice.dto;

import com.talentsync.recruitmentservice.entity.RecruitmentStage;
import jakarta.validation.constraints.NotNull;

public class UpdateStageRequest {

    @NotNull(message = "New stage is required")
    private RecruitmentStage stage;

    public UpdateStageRequest() {
    }

    public UpdateStageRequest(RecruitmentStage stage) {
        this.stage = stage;
    }

    public RecruitmentStage getStage() {
        return stage;
    }

    public void setStage(RecruitmentStage stage) {
        this.stage = stage;
    }
}
