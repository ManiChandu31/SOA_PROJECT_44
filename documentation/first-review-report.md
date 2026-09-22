# First Review Report

## Problem and Proposed System

Recruitment work is often split across email, spreadsheets, and disconnected job boards. TalentSync centralizes jobs, applications, pipeline progression, interview information, and offers for candidate and recruiter roles.

## Completed Implementation

| Area | Status |
|---|---|
| Eureka discovery and gateway routes | Implemented |
| JWT authentication and BCrypt passwords | Implemented |
| Job CRUD and public search | Implemented |
| Resume application submission with duplicate prevention | Implemented |
| Recruitment records, interviews and offers APIs | Implemented |
| React public experience and role-aware dashboard | Implemented |
| MySQL schema and Postman collection | Implemented |

## Planned Hardening

Recruiter ownership checks for every recruitment mutation, asynchronous retry/outbox delivery, object-storage resumes, service-to-service credentials, pagination, and expanded integration tests are planned next steps.

## Load Balancing

Run a second job instance with `mvn -f job-service/pom.xml spring-boot:run -Dspring-boot.run.arguments=--server.port=8085`. Registering both instances with Eureka lets Gateway and Feign select an instance by Spring Cloud LoadBalancer. This topology is configured but has not been runtime-verified in this workspace.
