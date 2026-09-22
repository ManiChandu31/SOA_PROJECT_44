package com.talentsync.authservice.controller;

import com.talentsync.authservice.dto.*;
import com.talentsync.authservice.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register/candidate")
    public ResponseEntity<AuthResponse> registerCandidate(@Valid @RequestBody CandidateRegisterRequest request) {
        AuthResponse response = authenticationService.registerCandidate(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register/recruiter")
    public ResponseEntity<AuthResponse> registerRecruiter(@Valid @RequestBody RecruiterRegisterRequest request) {
        AuthResponse response = authenticationService.registerRecruiter(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            Principal principal,
            Authentication authentication,
            @RequestHeader(value = "X-User-Email", required = false) String headerEmail
    ) {
        String email = null;
        if (principal != null) {
            email = principal.getName();
        } else if (authentication != null && authentication.getPrincipal() != null) {
            email = authentication.getName();
        } else if (headerEmail != null && !headerEmail.isBlank()) {
            email = headerEmail;
        }

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserResponse userResponse = authenticationService.getUserProfile(email);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse userResponse = authenticationService.getUserById(id);
        return ResponseEntity.ok(userResponse);
    }
}
