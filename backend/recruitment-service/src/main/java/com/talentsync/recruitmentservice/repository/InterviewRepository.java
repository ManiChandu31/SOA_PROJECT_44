package com.talentsync.recruitmentservice.repository;

import com.talentsync.recruitmentservice.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByApplicationId(Long applicationId);
    List<Interview> findByApplicationIdIn(List<Long> applicationIds);
}
