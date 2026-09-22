package com.talentsync.jobservice.repository;

import com.talentsync.jobservice.entity.EmploymentType;
import com.talentsync.jobservice.entity.Job;
import com.talentsync.jobservice.entity.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByStatus(JobStatus status);

    List<Job> findByRecruiterId(Long recruiterId);

    @Query("SELECT j FROM Job j WHERE (:status IS NULL OR j.status = :status) " +
           "AND (:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "    OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "    OR LOWER(j.requiredSkills) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:department IS NULL OR LOWER(j.department) = LOWER(:department)) " +
           "AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
           "AND (:employmentType IS NULL OR j.employmentType = :employmentType)")
    List<Job> searchJobs(
            @Param("keyword") String keyword,
            @Param("department") String department,
            @Param("location") String location,
            @Param("employmentType") EmploymentType employmentType,
            @Param("status") JobStatus status
    );
}
