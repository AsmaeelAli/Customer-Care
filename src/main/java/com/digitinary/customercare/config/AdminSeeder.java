package com.digitinary.customercare.config;

import com.digitinary.customercare.common.enums.Roles;
import com.digitinary.customercare.model.entities.systemuser.SystemUserEntity;
import com.digitinary.customercare.repository.SystemUserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AdminSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

    private final SystemUserRepo systemUserRepo;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(SystemUserRepo systemUserRepo, PasswordEncoder passwordEncoder) {
        this.systemUserRepo = systemUserRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (systemUserRepo.count() == 0) {
            String name = "Asmaeel";
            String username = "AsmaeelAli";
            String rawpassword = "asmaeel2001";
            String email = "asmaeel@gmail.com";
            String phone = "0790000000";

            SystemUserEntity admin = new SystemUserEntity(
                    name,
                    username,
                    passwordEncoder.encode(rawpassword),
                    email,
                    phone,
                    LocalDateTime.now(),
                    Roles.ADMIN
            );

            systemUserRepo.save(admin);

            log.info("=== Default admin created -> {} / {} ({})" , username , rawpassword , admin.getRole());

        }
    }
}