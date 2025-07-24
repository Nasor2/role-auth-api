package com.nasor.roleauthapi.infraestructure.config;

import com.nasor.roleauthapi.domain.Role;
import com.nasor.roleauthapi.domain.User;
import com.nasor.roleauthapi.domain.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminUserInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.admin-password}")
    private String initialAdminPassword;

    public AdminUserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner initAdminUser() {
        return (args) -> {
            if (!userRepository.existsByUsername("admin")) {
                User adminUser = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode(initialAdminPassword))
                        .firstName("Initial")
                        .lastName("Admin")
                        .role(Role.ADMIN)
                        .build();
                userRepository.save(adminUser);
                System.out.println("Initial admin user 'admin' created successfully!");
            } else {
                System.out.println("Admin user 'admin' already exists. Skipping creation.");
            }
        };
    }

}
