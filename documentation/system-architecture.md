# System Architecture

```mermaid
flowchart TB
  U[Candidate / Recruiter] --> F[React Frontend :5173]
  F --> G[API Gateway :8080]
  G --> A[Auth Service :8081]
  G --> J[Job Service :8082]
  G --> AP[Application Service :8083]
  G --> R[Recruitment Service :8084]
  AP -->|OpenFeign via Eureka| J
  AP -->|OpenFeign via Eureka| R
  A --> DA[(Auth DB)]
  J --> DJ[(Job DB)]
  AP --> DP[(Application DB)]
  R --> DR[(Recruitment DB)]
  E[Eureka :8761] --- A
  E --- J
  E --- AP
  E --- R
  E --- G
```

Each service owns its schema. Application Service stores IDs only, not cross-database JPA relationships. Eureka discovery resolves logical names such as `job-service`; the gateway uses `lb://` routes and Feign uses the registered service name.

```mermaid
sequenceDiagram
  participant C as Client
  participant G as Gateway
  participant A as Auth
  C->>G: POST /api/auth/login
  G->>A: Forward request
  A-->>C: JWT with userId and role
  C->>G: Protected request + Bearer JWT
  G->>G: Validate signature and expiry
  G-->>C: Route request or 401
```

```mermaid
sequenceDiagram
  participant C as Candidate
  participant AP as Application Service
  participant J as Job Service
  participant R as Recruitment Service
  C->>AP: Multipart resume and application
  AP->>J: Verify job
  AP->>AP: Save file and application
  AP->>R: Create tracking record
  R-->>AP: Created
  AP-->>C: 201 Created
  Note over AP,R: On tracking failure, application and local file are removed.
```
