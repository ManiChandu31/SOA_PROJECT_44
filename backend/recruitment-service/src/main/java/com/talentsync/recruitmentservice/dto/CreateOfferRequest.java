package com.talentsync.recruitmentservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateOfferRequest {

    @NotNull(message = "Offered salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Offered salary must be greater than zero")
    private BigDecimal offeredSalary;

    @NotNull(message = "Joining date is required")
    @Future(message = "Joining date must be in the future")
    private LocalDate joiningDate;

    private String offerDetails;

    public CreateOfferRequest() {
    }

    public CreateOfferRequest(BigDecimal offeredSalary, LocalDate joiningDate, String offerDetails) {
        this.offeredSalary = offeredSalary;
        this.joiningDate = joiningDate;
        this.offerDetails = offerDetails;
    }

    public BigDecimal getOfferedSalary() {
        return offeredSalary;
    }

    public void setOfferedSalary(BigDecimal offeredSalary) {
        this.offeredSalary = offeredSalary;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getOfferDetails() {
        return offerDetails;
    }

    public void setOfferDetails(String offerDetails) {
        this.offerDetails = offerDetails;
    }
}
