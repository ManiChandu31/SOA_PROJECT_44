package com.talentsync.jobservice.controller;

import com.talentsync.jobservice.dto.CreateJobRequest;
import com.talentsync.jobservice.dto.JobResponse;
import com.talentsync.jobservice.dto.UpdateJobRequest;
import com.talentsync.jobservice.entity.EmploymentType;
import com.talentsync.jobservice.exception.ForbiddenException;
import com.talentsync.jobservice.service.JobService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    @Value("${jwt.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String jwtSecret;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    private Long resolveUserId(String headerUserId, String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                Object userId = claims.get("userId");
                if (userId instanceof Number) {
                    return ((Number) userId).longValue();
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    private String resolveUserRole(String headerRole, String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                return claims.get("role", String.class);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    @PostMapping
    public ResponseEntity<JobResponse> createJob(
            @Valid @RequestBody CreateJobRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String headerUserId,
            @RequestHeader(value = "X-User-Role", required = false) String headerRole,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        Long recruiterId = resolveUserId(headerUserId, authHeader);
        String role = resolveUserRole(headerRole, authHeader);

        if (recruiterId == null || (!"RECRUITER".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role))) {
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
            @RequestHeader(value = "X-User-Id", required = false) String headerUserId,
            @RequestHeader(value = "X-User-Role", required = false) String headerRole,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        Long recruiterId = resolveUserId(headerUserId, authHeader);
        String role = resolveUserRole(headerRole, authHeader);

        if (recruiterId == null) {
            throw new ForbiddenException("Authentication required to update job");
        }

        JobResponse response = jobService.updateJob(jobId, request, recruiterId, role);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<JobResponse> deactivateJob(
            @PathVariable Long jobId,
            @RequestHeader(value = "X-User-Id", required = false) String headerUserId,
            @RequestHeader(value = "X-User-Role", required = false) String headerRole,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        Long recruiterId = resolveUserId(headerUserId, authHeader);
        String role = resolveUserRole(headerRole, authHeader);

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
            @RequestHeader(value = "X-User-Id", required = false) String headerUserId,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        Long recruiterId = resolveUserId(headerUserId, authHeader);
        if (recruiterId == null) {
            throw new ForbiddenException("Authentication required to view recruiter jobs");
        }
        return ResponseEntity.ok(jobService.getJobsByRecruiter(recruiterId));
    }
}
