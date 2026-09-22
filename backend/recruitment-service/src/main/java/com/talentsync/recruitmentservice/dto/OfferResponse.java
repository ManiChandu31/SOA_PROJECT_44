package com.talentsync.recruitmentservice.dto;

import com.talentsync.recruitmentservice.entity.Offer;
import com.talentsync.recruitmentservice.entity.OfferStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class OfferResponse {

    private Long id;
    private Long applicationId;
    private BigDecimal offeredSalary;
    private LocalDate joiningDate;
    private String offerDetails;
    private OfferStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OfferResponse() {
    }

    public OfferResponse(Offer offer) {
        this.id = offer.getId();
        this.applicationId = offer.getApplicationId();
        this.offeredSalary = offer.getOfferedSalary();
        this.joiningDate = offer.getJoiningDate();
        this.offerDetails = offer.getOfferDetails();
        this.status = offer.getStatus();
        this.createdAt = offer.getCreatedAt();
        this.updatedAt = offer.getUpdatedAt();
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

    public OfferStatus getStatus() {
        return status;
    }

    public void setStatus(OfferStatus status) {
        this.status = status;
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
