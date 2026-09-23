package com.talentsync.jobservice.controller;

import com.talentsync.jobservice.dto.CreateJobRequest;
import com.talentsync.jobservice.dto.JobResponse;
import com.talentsync.jobservice.dto.UpdateJobRequest;
import com.talentsync.jobservice.entity.EmploymentType;
import com.talentsync.jobservice.exception.ForbiddenException;
import com.talentsync.jobservice.service.JobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    private Long resolveUserId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        try {
            return Long.valueOf(authentication.getName());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private String resolveUserRole(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.substring(5))
                .findFirst()
                .orElse(null);
    }

    @PostMapping
    public ResponseEntity<JobResponse> createJob(
            @Valid @RequestBody CreateJobRequest request,
            Authentication authentication
    ) {
        Long recruiterId = resolveUserId(authentication);
        String role = resolveUserRole(authentication);

        if (recruiterId == null || !"RECRUITER".equalsIgnoreCase(role)) {
            throw new ForbiddenException("Only authorized recruiters can post jobs");
        }

        JobResponse response = jobService.createJob(request, recruiterId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllActiveJobs() {
        return ResponseEntity.ok(jobService.getAllActiveJobs());
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobService.getJobById(jobId));
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<JobResponse> updateJob(
            @PathVariable Long jobId,
            @Valid @RequestBody UpdateJobRequest request,
            Authentication authentication
    ) {
        Long recruiterId = resolveUserId(authentication);
        String role = resolveUserRole(authentication);

        if (recruiterId == null) {
            throw new ForbiddenException("Authentication required to update job");
        }

        JobResponse response = jobService.updateJob(jobId, request, recruiterId, role);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<JobResponse> deactivateJob(
            @PathVariable Long jobId,
            Authentication authentication
    ) {
        Long recruiterId = resolveUserId(authentication);
        String role = resolveUserRole(authentication);

        if (recruiterId == null) {
            throw new ForbiddenException("Authentication required to deactivate job");
        }

        JobResponse response = jobService.deactivateJob(jobId, recruiterId, role);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobResponse>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) EmploymentType employmentType
    ) {
        List<JobResponse> results = jobService.searchJobs(keyword, department, location, employmentType);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/recruiter/my-jobs")
    public ResponseEntity<List<JobResponse>> getMyJobs(
            Authentication authentication
    ) {
        Long recruiterId = resolveUserId(authentication);
        if (recruiterId == null) {
            throw new ForbiddenException("Authentication required to view recruiter jobs");
        }
        return ResponseEntity.ok(jobService.getJobsByRecruiter(recruiterId));
    }
}
