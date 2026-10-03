package com.digitinary.customercare.usecase.systemuser;

import com.digitinary.customercare.common.enums.Roles;
import com.digitinary.customercare.model.dto.login.RegisterRequestDto;
import com.digitinary.customercare.model.entities.systemuser.SystemUserEntity;
import com.digitinary.customercare.repository.SystemUserRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CreateSystemUser {

    private final SystemUserRepo systemUserRepo;
    private final PasswordEncoder passwordEncoder;

    public CreateSystemUser(
            SystemUserRepo systemUserRepo,
            PasswordEncoder passwordEncoder) {

        this.systemUserRepo = systemUserRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String execute(RegisterRequestDto request) {

        if (systemUserRepo.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already taken");
        }

        SystemUserEntity systemUser = new SystemUserEntity(
                request.name(),
                request.username(),
                passwordEncoder.encode(request.password()),
                request.email(),
                request.phone(),
                LocalDateTime.now(),
                Roles.USER
        );

        systemUserRepo.save(systemUser);

        return "System user created successfully";
    }
}
