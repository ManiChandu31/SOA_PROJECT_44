# Runtime Validation Report

**Generated**: 2026-10-29T17:15:00+05:30
**Target**: `C:\FSFD projects\TalentSync`

## Summary

| Step | Status | Exit Code | Details |
|---|---:|---:|---|
| Frontend build | PASS | 0 | Vite production build completed successfully |
| Frontend startup | PASS | 0 | Vite served `http://127.0.0.1:5173/` with HTTP 200 |
| Backend health | PASS | 0 | Gateway and job service responded; job service actuator health was UP |
| Browser E2E | PASS | 0 | 4 Playwright smoke tests passed |
| Dependency audit | PASS | 0 | `npm audit fix` completed with 0 vulnerabilities |

**Overall**: PASS

## Browser Journeys

1. Landing page to jobs page navigation.
2. Candidate/recruiter registration role switching.
3. Jobs page API integration and populated job-list rendering.
4. Candidate login with seeded credentials and dashboard navigation.
5. Populated job detail and application entry flow.

## Environment

- Node.js: 22.18.0
- npm: 10.9.3
- Docker: unavailable; local H2-backed services were used.
- Playwright Chromium: installed and available.
- Frontend URL: `http://127.0.0.1:5173/`
- Gateway URL: `http://localhost:8080/`

## Issues Found and Fixed

1. Replaced an unrelated stale `job-service` process occupying port 8082.
2. Added a safe local JWT fallback to the TalentSync job service configuration so it starts without an environment variable.
3. Added explicit local CORS configuration to the API gateway for Vite origins.
4. Added repeatable Playwright browser smoke tests.
5. Remediated frontend dependency advisories; final audit reports zero vulnerabilities.
6. Added idempotent local default candidate/recruiter users for login validation.
7. Added four idempotent professional default job listings for local development.

## Known Local-Development Limitation

Local H2 development data now includes four active jobs and two demo users. Production should use the configured MySQL databases and a private `JWT_SECRET`.

Demo credentials:

- Candidate: `candidate@example.com` / `Password123`
- Recruiter: `recruiter@talentsync.com` / `Password123`
