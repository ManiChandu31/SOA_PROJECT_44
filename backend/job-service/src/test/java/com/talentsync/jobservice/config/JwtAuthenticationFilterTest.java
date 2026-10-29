package com.talentsync.jobservice.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    private static final String SECRET = "test-secret-that-is-at-least-32-characters-long";

    private JwtAuthenticationFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter(SECRET);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void authenticatesRecruiterWithValidToken() throws ServletException, IOException {
        givenAuthorizationHeader(tokenWithClaims("42", "RECRUITER"));

        runFilter();

        assertAuthentication("42", "ROLE_RECRUITER");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void authenticatesCandidateWithNumericUserId() throws ServletException, IOException {
        givenAuthorizationHeader(tokenWithClaims(7L, "CANDIDATE"));

        runFilter();

        assertAuthentication("7", "ROLE_CANDIDATE");
    }

    @Test
    void continuesWithoutAuthenticationWhenAuthorizationHeaderIsMissing() throws ServletException, IOException {
        runFilter();

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void continuesWithoutAuthenticationWhenAuthorizationHeaderIsNotBearer() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Basic credentials");

        runFilter();

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void rejectsTokenSignedWithAnotherKey() throws ServletException, IOException {
        SecretKey anotherKey = Keys.hmacShaKeyFor(
                "another-test-secret-that-is-at-least-32-characters".getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder()
                .claim("userId", "42")
                .claim("role", "RECRUITER")
                .signWith(anotherKey, SignatureAlgorithm.HS256)
                .compact();
        givenAuthorizationHeader(token);

        runFilter();

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void rejectsTokenWithUnsupportedRole() throws ServletException, IOException {
        givenAuthorizationHeader(tokenWithClaims("42", "ADMIN"));

        runFilter();

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void rejectsTokenWithMissingOrInvalidUserId() throws ServletException, IOException {
        givenAuthorizationHeader(tokenWithClaims(null, "CANDIDATE"));
        runFilter();
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

        SecurityContextHolder.clearContext();
        givenAuthorizationHeader(tokenWithClaims("not-a-number", "CANDIDATE"));
        runFilter();
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void clearsExistingAuthenticationWhenTokenIsInvalid() throws ServletException, IOException {
        SecurityContextHolder.getContext().setAuthentication(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken("old-user", null));
        givenAuthorizationHeader("not-a-jwt");

        runFilter();

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void rejectsExpiredToken() throws ServletException, IOException {
        String token = Jwts.builder()
                .claim("userId", "42")
                .claim("role", "RECRUITER")
                .setExpiration(new Date(System.currentTimeMillis() - 1_000))
                .signWith(signingKey(), SignatureAlgorithm.HS256)
                .compact();
        givenAuthorizationHeader(token);

        runFilter();

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    private void runFilter() throws ServletException, IOException {
        filter.doFilterInternal(request, response, filterChain);
    }

    private void givenAuthorizationHeader(String token) {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
    }

    private String tokenWithClaims(Object userId, String role) {
        var builder = Jwts.builder().claim("role", role);
        if (userId != null) {
            builder.claim("userId", userId);
        }
        return builder.signWith(signingKey(), SignatureAlgorithm.HS256).compact();
    }

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    private void assertAuthentication(String expectedPrincipal, String expectedAuthority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo(expectedPrincipal);
        assertThat(authentication.getAuthorities())
                .extracting("authority")
                .containsExactly(expectedAuthority);
    }
}
