package com.talentsync.authservice.config;

import com.talentsync.authservice.entity.Role;
import com.talentsync.authservice.entity.User;
import com.talentsync.authservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DefaultUserDataLoader {

    @Bean
    CommandLineRunner loadDefaultUsers(UserRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!repository.existsByEmail("candidate@example.com")) {
                repository.save(new User(null, "Alex Morgan", "candidate@example.com",
                        passwordEncoder.encode("Password123"), Role.CANDIDATE, null));
            }
            if (!repository.existsByEmail("recruiter@talentsync.com")) {
                repository.save(new User(null, "Sarah Jenkins", "recruiter@talentsync.com",
                        passwordEncoder.encode("Password123"), Role.RECRUITER, "Global Tech Innovations"));
            }
        };
    }
}
