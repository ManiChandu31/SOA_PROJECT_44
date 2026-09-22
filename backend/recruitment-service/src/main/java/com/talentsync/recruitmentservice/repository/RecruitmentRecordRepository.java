package com.talentsync.recruitmentservice.repository;

import com.talentsync.recruitmentservice.entity.RecruitmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruitmentRecordRepository extends JpaRepository<RecruitmentRecord, Long> {
    Optional<RecruitmentRecord> findByApplicationId(Long applicationId);
    List<RecruitmentRecord> findByCandidateId(Long candidateId);
    List<RecruitmentRecord> findByJobId(Long jobId);
    boolean existsByApplicationId(Long applicationId);
}
