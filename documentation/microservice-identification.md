# Microservice Identification

| Service | Responsibility | Database |
|---|---|---|
| Auth Service | Registration, login, JWT profiles | talentsync_auth_db |
| Job Service | Job lifecycle and public search | talentsync_job_db |
| Application Service | Resume storage and applications | talentsync_application_db |
| Recruitment Service | Pipeline, interviews, offers | talentsync_recruitment_db |
| Eureka Server | Discovery registry | None |
| API Gateway | JWT validation and routing | None |

Services register with Eureka and use logical names. Application Service uses OpenFeign for Job and Recruitment calls.
