package com.talentsync.applicationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talentsync.applicationservice.dto.ApplicationResponse;
import com.talentsync.applicationservice.entity.ApplicationStatus;
import com.talentsync.applicationservice.service.ApplicationService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApplicationController.class)
@TestPropertySource(properties = "jwt.secret=abcdefghijklmnopqrstuvwxyz1234567890ABCDEF")
class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService applicationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void candidateApplicationsEndpointShouldReturnCandidateApplications() throws Exception {
        ApplicationResponse response = new ApplicationResponse(
                7L,
                2L,
                12L,
                "resume.pdf",
                "Interested in the role.",
                ApplicationStatus.SHORTLISTED,
                Instant.parse("2026-09-21T22:00:00Z"),
                Instant.parse("2026-09-21T22:05:00Z")
        );

        given(applicationService.mine(2L)).willReturn(List.of(response));

        String token = Jwts.builder()
                .claim("userId", 2L)
                .claim("role", "CANDIDATE")
                .signWith(Keys.hmacShaKeyFor("abcdefghijklmnopqrstuvwxyz1234567890ABCDEF".getBytes(StandardCharsets.UTF_8)))
                .compact();

        mockMvc.perform(get("/api/applications/candidate/2")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].candidateId").value(2))
                .andExpect(jsonPath("$[0].jobId").value(12));
    }
}
