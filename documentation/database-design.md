# Database Design

Each business service owns a MySQL schema. Cross-service values such as `candidateId`, `jobId`, and `applicationId` are stored as IDs, not JPA foreign-key relationships. See `database/schema.sql` for tables, unique constraints, indexes, and seed data.

Credentials are provided through `DB_HOST`, `DB_PORT`, `DB_USERNAME`, and `DB_PASSWORD` environment variables. No database password is stored in repository configuration.
