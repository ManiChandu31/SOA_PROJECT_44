package com.talentsync.recruitmentservice.repository;

import com.talentsync.recruitmentservice.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    Optional<Offer> findByApplicationId(Long applicationId);
    List<Offer> findByApplicationIdIn(List<Long> applicationIds);
    boolean existsByApplicationId(Long applicationId);
}
