package com.talentsync.authservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talentsync.authservice.dto.CandidateRegisterRequest;
import com.talentsync.authservice.dto.LoginRequest;
import com.talentsync.authservice.dto.RecruiterRegisterRequest;
import com.talentsync.authservice.entity.Role;
import com.talentsync.authservice.entity.User;
import com.talentsync.authservice.repository.UserRepository;
import com.talentsync.authservice.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthServiceTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("TC-AUTH-01: Candidate Registration Success")
    void testRegisterCandidateSuccess() throws Exception {
        CandidateRegisterRequest request = new CandidateRegisterRequest(
                "John Doe",
                "john.doe@example.com",
                "Password123",
                "Password123"
        );

        mockMvc.perform(post("/api/auth/register/candidate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")))
                .andExpect(jsonPath("$.role", is("CANDIDATE")))
                .andExpect(jsonPath("$.fullName", is("John Doe")));

        User savedUser = userRepository.findByEmail("john.doe@example.com").orElse(null);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getRole()).isEqualTo(Role.CANDIDATE);
        assertThat(passwordEncoder.matches("Password123", savedUser.getPassword())).isTrue();
    }

    @Test
    @DisplayName("TC-AUTH-02: Recruiter Registration Success")
    void testRegisterRecruiterSuccess() throws Exception {
        RecruiterRegisterRequest request = new RecruiterRegisterRequest(
                "Sarah Connor",
                "sarah.connor@cyberdyne.com",
                "Password123",
                "Password123",
                "Cyberdyne Systems"
        );

        mockMvc.perform(post("/api/auth/register/recruiter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.email", is("sarah.connor@cyberdyne.com")))
                .andExpect(jsonPath("$.role", is("RECRUITER")))
                .andExpect(jsonPath("$.organizationName", is("Cyberdyne Systems")));

        User savedUser = userRepository.findByEmail("sarah.connor@cyberdyne.com").orElse(null);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getRole()).isEqualTo(Role.RECRUITER);
        assertThat(savedUser.getOrganizationName()).isEqualTo("Cyberdyne Systems");
    }

    @Test
    @DisplayName("TC-AUTH-03: Duplicate Email Registration Rejection")
    void testRegisterDuplicateEmailFails() throws Exception {
        CandidateRegisterRequest request1 = new CandidateRegisterRequest(
                "Alice Wonder",
                "alice@example.com",
                "Password123",
                "Password123"
        );
        mockMvc.perform(post("/api/auth/register/candidate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isCreated());

        CandidateRegisterRequest request2 = new CandidateRegisterRequest(
                "Alice Duplicate",
                "alice@example.com",
                "DifferentPassword",
                "DifferentPassword"
        );
        mockMvc.perform(post("/api/auth/register/candidate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Email already registered")));
    }

    @Test
    @DisplayName("TC-AUTH-04: Password Confirmation Mismatch")
    void testPasswordMismatchFails() throws Exception {
        CandidateRegisterRequest request = new CandidateRegisterRequest(
                "Bob Smith",
                "bob@example.com",
                "Password123",
                "Mismatch321"
        );

        mockMvc.perform(post("/api/auth/register/candidate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("do not match")));
    }

    @Test
    @DisplayName("TC-AUTH-05: Successful Login & JWT Generation")
    void testLoginSuccess() throws Exception {
        CandidateRegisterRequest register = new CandidateRegisterRequest(
                "Elena Fisher",
                "elena@example.com",
                "Password123",
                "Password123"
        );
        mockMvc.perform(post("/api/auth/register/candidate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated());

        LoginRequest loginRequest = new LoginRequest("elena@example.com", "Password123");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.role", is("CANDIDATE")));
    }

    @Test
    @DisplayName("TC-AUTH-06: Invalid Password Login Rejection")
    void testLoginInvalidPasswordFails() throws Exception {
        CandidateRegisterRequest register = new CandidateRegisterRequest(
                "Elena Fisher",
                "elena@example.com",
                "Password123",
                "Password123"
        );
        mockMvc.perform(post("/api/auth/register/candidate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated());

        LoginRequest loginRequest = new LoginRequest("elena@example.com", "WrongPassword");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Invalid email or password")));
    }

    @Test
    @DisplayName("TC-AUTH-07: Get Authenticated User Profile via JWT")
    void testGetProfileWithJwt() throws Exception {
        CandidateRegisterRequest register = new CandidateRegisterRequest(
                "Nathan Drake",
                "drake@example.com",
                "Password123",
                "Password123"
        );
        String responseStr = mockMvc.perform(post("/api/auth/register/candidate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String token = objectMapper.readTree(responseStr).get("token").asText();

        mockMvc.perform(get("/api/auth/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("drake@example.com")))
                .andExpect(jsonPath("$.fullName", is("Nathan Drake")))
                .andExpect(jsonPath("$.role", is("CANDIDATE")));
    }

    @Test
    @DisplayName("TC-AUTH-08: Access /api/auth/me without Token returns 401")
    void testGetProfileWithoutTokenFails() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized());
    }
}
