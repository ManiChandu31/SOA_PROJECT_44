# JWT Authentication

Auth Service hashes passwords with BCrypt. A successful login returns an HS256 JWT containing the user's ID, email subject, and role. The API Gateway validates signature and expiry before routing protected requests. Job and Application services also validate the bearer token before taking ownership-sensitive actions.

Use Swagger's **Authorize** control and enter the JWT returned from `/api/auth/login` to call protected operations.
