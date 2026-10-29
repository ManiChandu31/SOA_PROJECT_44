package com.talentsync.jobservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talentsync.jobservice.dto.CreateJobRequest;
import com.talentsync.jobservice.dto.UpdateJobRequest;
import com.talentsync.jobservice.entity.EmploymentType;
import com.talentsync.jobservice.entity.Job;
import com.talentsync.jobservice.entity.JobStatus;
import com.talentsync.jobservice.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JobServiceTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        jobRepository.deleteAll();
    }

    private CreateJobRequest buildSampleJobRequest() {
        return new CreateJobRequest(
                "Full Stack Engineer",
                "Engineering",
                "Remote",
                EmploymentType.FULL_TIME,
                "Build modern microservices and React applications",
                "Java, Spring Boot, React",
                3,
                new BigDecimal("90000"),
                new BigDecimal("120000"),
                2,
                LocalDate.now().plusMonths(2)
        );
    }

    private String token(long userId, String role) {
        return Jwts.builder().setSubject("test@talentsync.local")
                .claim("userId", userId).claim("role", role)
                .setExpiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(Keys.hmacShaKeyFor("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970".getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    @Test
    @DisplayName("TC-JOB-01: Create Job Success as Recruiter")
    void testCreateJobSuccess() throws Exception {
        CreateJobRequest request = buildSampleJobRequest();

        mockMvc.perform(post("/api/jobs")
                        .header("Authorization", "Bearer " + token(101, "RECRUITER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("Full Stack Engineer")))
                .andExpect(jsonPath("$.recruiterId", is(101)))
                .andExpect(jsonPath("$.status", is("ACTIVE")));

        assertThat(jobRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("TC-JOB-02: Create Job Rejected for Candidate Role")
    void testCreateJobForbiddenForCandidate() throws Exception {
        CreateJobRequest request = buildSampleJobRequest();

        mockMvc.perform(post("/api/jobs")
                        .header("Authorization", "Bearer " + token(202, "CANDIDATE"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("TC-JOB-02A: Protected Job Operations Reject Missing and Invalid Tokens")
    void testProtectedOperationsRejectMissingAndInvalidTokens() throws Exception {
        CreateJobRequest request = buildSampleJobRequest();

        mockMvc.perform(post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/jobs")
                        .header("Authorization", "Bearer not-a-jwt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("TC-JOB-02C: Swagger entry page is public")
    void testSwaggerEntryPageIsPublic() throws Exception {
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("TC-JOB-02B: Client Identity Headers Cannot Grant Recruiter Access")
    void testClientIdentityHeadersCannotGrantRecruiterAccess() throws Exception {
        CreateJobRequest request = buildSampleJobRequest();

        mockMvc.perform(post("/api/jobs")
                        .header("X-User-Id", "101")
                        .header("X-User-Role", "RECRUITER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/jobs")
                        .header("Authorization", "Bearer " + token(101, "ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("TC-JOB-03: Retrieve Active Jobs and Job By ID")
    void testGetActiveJobsAndById() throws Exception {
        Job job = new Job(
                null, "DevOps Engineer", "Infrastructure", "Austin, TX",
                EmploymentType.FULL_TIME, "Manage CI/CD", "AWS, Docker",
                4, new BigDecimal("110000"), new BigDecimal("135000"),
                1, LocalDate.now().plusMonths(1), JobStatus.ACTIVE, 101L
        );
        Job saved = jobRepository.save(job);

        mockMvc.perform(get("/api/jobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("DevOps Engineer")));

        mockMvc.perform(get("/api/jobs/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(saved.getId().intValue())))
                .andExpect(jsonPath("$.title", is("DevOps Engineer")));
    }

    @Test
    @DisplayName("TC-JOB-04: Update Job Ownership Enforcement")
    void testUpdateJobOwnership() throws Exception {
        Job job = new Job(
                null, "Backend Developer", "Engineering", "Chicago, IL",
                EmploymentType.FULL_TIME, "Java APIs", "Java, Spring",
                2, new BigDecimal("80000"), new BigDecimal("100000"),
                1, LocalDate.now().plusMonths(1), JobStatus.ACTIVE, 101L
        );
        Job saved = jobRepository.save(job);

        UpdateJobRequest updateRequest = new UpdateJobRequest();
        updateRequest.setTitle("Senior Backend Developer");
        updateRequest.setDepartment("Engineering");
        updateRequest.setLocation("Chicago, IL");
        updateRequest.setEmploymentType(EmploymentType.FULL_TIME);
        updateRequest.setDescription("Updated description");
        updateRequest.setRequiredSkills("Java, Spring, Microservices");
        updateRequest.setMinimumExperience(5);
        updateRequest.setSalaryMin(new BigDecimal("110000"));
        updateRequest.setSalaryMax(new BigDecimal("140000"));
        updateRequest.setNumberOfOpenings(2);
        updateRequest.setApplicationDeadline(LocalDate.now().plusMonths(2));
        updateRequest.setStatus(JobStatus.ACTIVE);

        // Attempt edit by different recruiter (ID 999) -> Forbidden
        mockMvc.perform(put("/api/jobs/" + saved.getId())
                        .header("Authorization", "Bearer " + token(999, "RECRUITER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isForbidden());

        // Attempt edit by owner recruiter (ID 101) -> Success
        mockMvc.perform(put("/api/jobs/" + saved.getId())
                        .header("Authorization", "Bearer " + token(101, "RECRUITER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Senior Backend Developer")))
                .andExpect(jsonPath("$.minimumExperience", is(5)));
    }

    @Test
    @DisplayName("TC-JOB-05: Deactivate Job")
    void testDeactivateJob() throws Exception {
        Job job = new Job(
                null, "QA Automation Engineer", "Quality", "Remote",
                EmploymentType.CONTRACT, "Automation testing", "Selenium, JUnit",
                2, new BigDecimal("70000"), new BigDecimal("90000"),
                1, LocalDate.now().plusMonths(1), JobStatus.ACTIVE, 101L
        );
        Job saved = jobRepository.save(job);

        mockMvc.perform(delete("/api/jobs/" + saved.getId())
                        .header("Authorization", "Bearer " + token(101, "RECRUITER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("INACTIVE")));

        Job updated = jobRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(JobStatus.INACTIVE);
    }

    @Test
    @DisplayName("TC-JOB-06: Search Jobs by Keyword and Filters")
    void testSearchJobs() throws Exception {
        Job job1 = new Job(
                null, "React Frontend Specialist", "Design", "Boston, MA",
                EmploymentType.FULL_TIME, "Build UI", "React, TypeScript",
                3, new BigDecimal("95000"), new BigDecimal("120000"),
                1, LocalDate.now().plusMonths(1), JobStatus.ACTIVE, 101L
        );
        Job job2 = new Job(
                null, "Python Data Scientist", "Data", "Remote",
                EmploymentType.CONTRACT, "ML models", "Python, PyTorch",
                4, new BigDecimal("130000"), new BigDecimal("160000"),
                1, LocalDate.now().plusMonths(1), JobStatus.ACTIVE, 101L
        );
        jobRepository.save(job1);
        jobRepository.save(job2);

        mockMvc.perform(get("/api/jobs/search?keyword=React"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("React Frontend Specialist")));

        mockMvc.perform(get("/api/jobs/search?location=Boston"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].location", is("Boston, MA")));
    }
}
