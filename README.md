# TalentSync Global Recruitment

TalentSync is a Spring Boot microservices and React application for managing job postings, candidate applications, recruitment pipelines, interviews, and offers.

## Services

| Service | Port | Responsibility |
|---|---:|---|
| Eureka Server | 8761 | Service discovery dashboard |
| API Gateway | 8080 | Public API routing and JWT validation |
| Auth Service | 8081 | Candidate/recruiter registration and login |
| Job Service | 8082 | Job posting and search |
| Application Service | 8083 | Resume uploads and applications |
| Recruitment Service | 8084 | Pipeline, interview, and offer records |

## Quick Start (Windows)

1. Install Java 21, Maven 3.9+, Node.js 20+ and MySQL 8.
2. Run `mysql -u root -p < database/schema.sql` from the project root.
3. Copy `.env.example` values into your shell environment. Replace `JWT_SECRET` in any non-demo environment.
4. Start services in this order from `backend`: `mvn -f eureka-server/pom.xml spring-boot:run`, then auth, job, recruitment, application, and finally `mvn -f api-gateway/pom.xml spring-boot:run`.
5. From `frontend`, run `npm install` then `npm run dev`. Visit `http://localhost:5173`.

The Eureka dashboard is `http://localhost:8761`. The frontend only calls the gateway at `http://localhost:8080`.

## Security

Auth Service uses BCrypt password hashes and signs JWTs containing `userId` and `role`. The gateway validates each non-public request and forwards verified identity headers. Job and application actions also verify the signed token, and applications derive the candidate identity from it rather than the submitted form.

## Swagger API Documentation

Swagger replaces Postman for API demonstrations. Once services are running, use these browser documentation pages:

- Gateway: `http://localhost:8080/swagger-ui.html`
- Auth: `http://localhost:8081/swagger-ui.html`
- Job: `http://localhost:8082/swagger-ui.html`
- Application: `http://localhost:8083/swagger-ui.html`
- Recruitment: `http://localhost:8084/swagger-ui.html`

The generated OpenAPI JSON is at `/v3/api-docs` on each service port.

## API Summary

- `POST /api/auth/register/candidate`, `POST /api/auth/register/recruiter`, `POST /api/auth/login`, `GET /api/auth/me`
- `GET|POST /api/jobs`, `GET|PUT|DELETE /api/jobs/{id}`, `GET /api/jobs/search`
- `POST /api/applications`, `GET /api/applications/my-applications`, `GET /api/applications/{id}/resume`
- `POST /api/recruitment/records`, `PUT /api/recruitment/application/{id}/stage`, interview and offer routes under `/api/recruitment`

## Verification

`npm run build` completed successfully on 2026-09-21. Backend Maven tests were not executed: the included Maven wrapper could not bootstrap in this workspace and Maven is not installed globally. Install Maven and run `mvn -f <service>/pom.xml test` for each service.

## Limitations

This academic implementation uses local resume storage and shared JWT configuration between services. Before production, place uploads in managed object storage, rotate secrets through a vault, add inter-service credentials, and broaden controller/integration test coverage.
