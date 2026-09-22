package com.talentsync.jobservice.service;

import com.talentsync.jobservice.dto.CreateJobRequest;
import com.talentsync.jobservice.dto.JobResponse;
import com.talentsync.jobservice.dto.UpdateJobRequest;
import com.talentsync.jobservice.entity.EmploymentType;
import com.talentsync.jobservice.entity.Job;
import com.talentsync.jobservice.entity.JobStatus;
import com.talentsync.jobservice.exception.BadRequestException;
import com.talentsync.jobservice.exception.ForbiddenException;
import com.talentsync.jobservice.exception.ResourceNotFoundException;
import com.talentsync.jobservice.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Transactional
    public JobResponse createJob(CreateJobRequest request, Long recruiterId) {
        if (recruiterId == null) {
            throw new ForbiddenException("Recruiter ID is required to create a job");
        }

        if (request.getSalaryMin() != null && request.getSalaryMax() != null) {
            if (request.getSalaryMin().compareTo(request.getSalaryMax()) > 0) {
                throw new BadRequestException("Minimum salary cannot be greater than maximum salary");
            }
        }

        Job job = new Job();
        job.setTitle(request.getTitle().trim());
        job.setDepartment(request.getDepartment().trim());
        job.setLocation(request.getLocation().trim());
        job.setEmploymentType(request.getEmploymentType());
        job.setDescription(request.getDescription().trim());
        job.setRequiredSkills(request.getRequiredSkills().trim());
        job.setMinimumExperience(request.getMinimumExperience());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setNumberOfOpenings(request.getNumberOfOpenings());
        job.setApplicationDeadline(request.getApplicationDeadline());
        job.setStatus(JobStatus.ACTIVE);
        job.setRecruiterId(recruiterId);

        Job savedJob = jobRepository.save(job);
        return new JobResponse(savedJob);
    }

    public List<JobResponse> getAllActiveJobs() {
        return jobRepository.findByStatus(JobStatus.ACTIVE).stream()
                .map(JobResponse::new)
                .collect(Collectors.toList());
    }

    public JobResponse getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
        return new JobResponse(job);
    }

    @Transactional
    public JobResponse updateJob(Long jobId, UpdateJobRequest request, Long recruiterId, String role) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        validateOwnership(job, recruiterId, role);

        if (request.getSalaryMin() != null && request.getSalaryMax() != null) {
            if (request.getSalaryMin().compareTo(request.getSalaryMax()) > 0) {
                throw new BadRequestException("Minimum salary cannot be greater than maximum salary");
            }
        }

        job.setTitle(request.getTitle().trim());
        job.setDepartment(request.getDepartment().trim());
        job.setLocation(request.getLocation().trim());
        job.setEmploymentType(request.getEmploymentType());
        job.setDescription(request.getDescription().trim());
        job.setRequiredSkills(request.getRequiredSkills().trim());
        job.setMinimumExperience(request.getMinimumExperience());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setNumberOfOpenings(request.getNumberOfOpenings());
        job.setApplicationDeadline(request.getApplicationDeadline());
        job.setStatus(request.getStatus());

        Job updatedJob = jobRepository.save(job);
        return new JobResponse(updatedJob);
    }

    @Transactional
    public JobResponse deactivateJob(Long jobId, Long recruiterId, String role) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        validateOwnership(job, recruiterId, role);

        job.setStatus(JobStatus.INACTIVE);
        Job savedJob = jobRepository.save(job);
        return new JobResponse(savedJob);
    }

    public List<JobResponse> getJobsByRecruiter(Long recruiterId) {
        if (recruiterId == null) {
            throw new ForbiddenException("Recruiter identity is required");
        }
        return jobRepository.findByRecruiterId(recruiterId).stream()
                .map(JobResponse::new)
                .collect(Collectors.toList());
    }

    public List<JobResponse> searchJobs(String keyword, String department, String location, EmploymentType employmentType) {
        String cleanKeyword = (keyword != null && !keyword.isBlank()) ? keyword.trim() : null;
        String cleanDept = (department != null && !department.isBlank()) ? department.trim() : null;
        String cleanLoc = (location != null && !location.isBlank()) ? location.trim() : null;

        return jobRepository.searchJobs(cleanKeyword, cleanDept, cleanLoc, employmentType, JobStatus.ACTIVE).stream()
                .map(JobResponse::new)
                .collect(Collectors.toList());
    }

    private void validateOwnership(Job job, Long recruiterId, String role) {
        if ("ADMIN".equalsIgnoreCase(role)) {
            return;
        }
        if (recruiterId == null || !job.getRecruiterId().equals(recruiterId)) {
            throw new ForbiddenException("Unauthorized: You do not have permission to modify this job posting");
        }
    }
}
