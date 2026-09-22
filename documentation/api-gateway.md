# API Gateway

Gateway runs on port 8080 and routes `/api/auth/**`, `/api/jobs/**`, `/api/applications/**`, and `/api/recruitment/**` through Eureka load-balanced service names. Authentication registration/login and public job search routes are public. Other routes require a valid bearer JWT.

Swagger: `http://localhost:8080/swagger-ui.html`.
