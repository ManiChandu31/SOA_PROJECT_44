-- ====================================================================
-- TalentSync Global Recruitment - Enterprise Database Schema
-- PS044: Enterprise Talent Acquisition & Candidate Pipeline Orchestration Engine
-- ====================================================================

-- --------------------------------------------------------------------
-- 1. AUTH SERVICE DATABASE (talentsync_auth_db)
-- --------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS talentsync_auth_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE talentsync_auth_db;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    organization_name VARCHAR(100) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_email (email),
    INDEX idx_user_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Sample Seed Users (BCrypt encrypted password: 'Password123')
-- Hash: $2a$10$lgIOXGXQs7xspR5sXts/POilrL.Ug0KfXixWEnsv9O95WabCXz9fG
INSERT IGNORE INTO users (id, full_name, email, password, role, organization_name)
VALUES 
(1, 'Sarah Jenkins', 'recruiter@talentsync.com', '$2a$10$lgIOXGXQs7xspR5sXts/POilrL.Ug0KfXixWEnsv9O95WabCXz9fG', 'RECRUITER', 'Global Tech Innovations'),
(2, 'Alex Morgan', 'candidate@example.com', '$2a$10$lgIOXGXQs7xspR5sXts/POilrL.Ug0KfXixWEnsv9O95WabCXz9fG', 'CANDIDATE', NULL);

-- --------------------------------------------------------------------
-- 2. JOB SERVICE DATABASE (talentsync_job_db)
-- --------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS talentsync_job_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE talentsync_job_db;

CREATE TABLE IF NOT EXISTS jobs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    department VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL,
    employment_type VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    required_skills VARCHAR(500) NOT NULL,
    minimum_experience INT NOT NULL DEFAULT 0,
    salary_min DECIMAL(12, 2) NULL,
    salary_max DECIMAL(12, 2) NULL,
    number_of_openings INT NOT NULL DEFAULT 1,
    application_deadline DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    recruiter_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_jobs_status (status),
    INDEX idx_jobs_recruiter (recruiter_id),
    INDEX idx_jobs_department (department),
    INDEX idx_jobs_location (location),
    INDEX idx_jobs_type (employment_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Sample Seed Jobs
INSERT IGNORE INTO jobs (id, title, department, location, employment_type, description, required_skills, minimum_experience, salary_min, salary_max, number_of_openings, application_deadline, status, recruiter_id)
VALUES 
(1, 'Senior Java Cloud Architect', 'Engineering', 'New York, NY (Hybrid)', 'FULL_TIME', 'Lead the design and implementation of next-generation microservices using Spring Boot, Spring Cloud, Docker, and Kubernetes.', 'Java 21, Spring Boot, Microservices, Docker, Kubernetes, MySQL', 6, 140000.00, 185000.00, 2, '2026-12-31', 'ACTIVE', 1),
(2, 'Full Stack React & Spring Developer', 'Engineering', 'San Francisco, CA (Remote)', 'FULL_TIME', 'Design modern user interfaces in React and build robust RESTful APIs in Spring Boot.', 'React, JavaScript, Vite, Spring Boot, REST APIs, CSS', 3, 105000.00, 145000.00, 3, '2026-11-30', 'ACTIVE', 1),
(3, 'DevOps & Site Reliability Engineer', 'Infrastructure', 'Austin, TX', 'FULL_TIME', 'Orchestrate CI/CD pipelines, monitor microservice health, and ensure high availability across global cloud regions.', 'AWS, Docker, Jenkins, Terraform, Prometheus, Grafana', 4, 120000.00, 160000.00, 1, '2026-10-31', 'ACTIVE', 1);

-- --------------------------------------------------------------------
-- 3. APPLICATION SERVICE DATABASE (talentsync_application_db)
-- --------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS talentsync_application_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE talentsync_application_db;

CREATE TABLE IF NOT EXISTS applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    candidate_id BIGINT NOT NULL,
    job_id BIGINT NOT NULL,
    resume_file_name VARCHAR(255) NOT NULL,
    resume_storage_path VARCHAR(500) NOT NULL,
    cover_letter TEXT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'APPLIED',
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_candidate_job (candidate_id, job_id),
    INDEX idx_app_candidate (candidate_id),
    INDEX idx_app_job (job_id),
    INDEX idx_app_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Sample Seed Application
INSERT IGNORE INTO applications (id, candidate_id, job_id, resume_file_name, resume_storage_path, cover_letter, status)
VALUES
(1, 2, 1, 'alex_morgan_resume.pdf', 'uploads/resumes/alex_morgan_resume.pdf', 'Enthusiastic cloud architect with deep experience in Spring Cloud and microservices.', 'SHORTLISTED');

-- --------------------------------------------------------------------
-- 4. RECRUITMENT SERVICE DATABASE (talentsync_recruitment_db)
-- --------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS talentsync_recruitment_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE talentsync_recruitment_db;

CREATE TABLE IF NOT EXISTS recruitment_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    application_id BIGINT NOT NULL UNIQUE,
    candidate_id BIGINT NOT NULL,
    job_id BIGINT NOT NULL,
    current_stage VARCHAR(30) NOT NULL DEFAULT 'APPLIED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_rec_candidate (candidate_id),
    INDEX idx_rec_job (job_id),
    INDEX idx_rec_stage (current_stage)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS interviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    application_id BIGINT NOT NULL,
    round VARCHAR(50) NOT NULL,
    scheduled_date_time DATETIME NOT NULL,
    interview_mode VARCHAR(20) NOT NULL DEFAULT 'ONLINE',
    meeting_link VARCHAR(255) NULL,
    location VARCHAR(255) NULL,
    interviewer_name VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    feedback TEXT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_int_app (application_id),
    INDEX idx_int_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS offers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    application_id BIGINT NOT NULL UNIQUE,
    offered_salary DECIMAL(12, 2) NOT NULL,
    joining_date DATE NOT NULL,
    offer_details TEXT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_offer_app (application_id),
    INDEX idx_offer_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Sample Seed Pipeline Data
INSERT IGNORE INTO recruitment_records (id, application_id, candidate_id, job_id, current_stage)
VALUES 
(1, 1, 2, 1, 'SHORTLISTED');

INSERT IGNORE INTO interviews (id, application_id, round, scheduled_date_time, interview_mode, meeting_link, interviewer_name, status, feedback)
VALUES
(1, 1, 'Technical Round 1', '2026-10-05 14:00:00', 'ONLINE', 'https://meet.talentsync.com/tech-round-1', 'David Ross (Principal Engineer)', 'SCHEDULED', NULL);
