package com.talentsync.jobservice.config;

import com.talentsync.jobservice.entity.EmploymentType;
import com.talentsync.jobservice.entity.Job;
import com.talentsync.jobservice.entity.JobStatus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Configuration
public class DefaultJobDataLoader {

    @Bean
    CommandLineRunner loadDefaultJobs(com.talentsync.jobservice.repository.JobRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }

            repository.saveAll(List.of(
                    job("Senior Java Cloud Architect", "Engineering", "New York, NY (Hybrid)",
                            EmploymentType.FULL_TIME,
                            "Lead the design and implementation of cloud-native microservices for a global platform.",
                            "Java 21, Spring Boot, Spring Cloud, Docker, Kubernetes, MySQL", 6,
                            "140000", "185000", 2, 63),
                    job("Full Stack React & Spring Developer", "Engineering", "San Francisco, CA (Remote)",
                            EmploymentType.REMOTE,
                            "Build accessible React experiences and reliable Spring Boot APIs for high-growth products.",
                            "React, JavaScript, Vite, Spring Boot, REST APIs, CSS", 3,
                            "105000", "145000", 3, 42),
                    job("DevOps & Site Reliability Engineer", "Infrastructure", "Austin, TX",
                            EmploymentType.FULL_TIME,
                            "Improve delivery automation, observability, and resilience across a distributed service platform.",
                            "AWS, Docker, Jenkins, Terraform, Prometheus, Grafana", 4,
                            "120000", "160000", 1, 28),
                    job("Product Design Lead", "Design", "Remote",
                            EmploymentType.REMOTE,
                            "Shape end-to-end product experiences with research-led design and a scalable design system.",
                            "Figma, UX Research, Design Systems, Prototyping, Accessibility", 5,
                            "115000", "155000", 1, 56)
            ));
        };
    }

    private Job job(String title, String department, String location, EmploymentType type,
                    String description, String skills, int experience, String salaryMin,
                    String salaryMax, int openings, int deadlineDays) {
        return new Job(null, title, department, location, type, description, skills, experience,
                new BigDecimal(salaryMin), new BigDecimal(salaryMax), openings,
                LocalDate.now().plusDays(deadlineDays), JobStatus.ACTIVE, 1L);
    }
}
