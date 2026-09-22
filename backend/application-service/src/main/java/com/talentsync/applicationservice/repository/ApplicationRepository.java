package com.talentsync.applicationservice.repository;
import com.talentsync.applicationservice.entity.JobApplication; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface ApplicationRepository extends JpaRepository<JobApplication,Long>{ boolean existsByCandidateIdAndJobId(Long candidateId,Long jobId); List<JobApplication> findByCandidateIdOrderByAppliedAtDesc(Long candidateId); List<JobApplication> findByJobIdOrderByAppliedAtDesc(Long jobId); }
