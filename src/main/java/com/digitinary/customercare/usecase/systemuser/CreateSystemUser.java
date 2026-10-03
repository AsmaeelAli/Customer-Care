package com.digitinary.customercare.usecase.systemuser;

import com.digitinary.customercare.common.enums.Roles;
import com.digitinary.customercare.model.dto.login.RegisterRequestDto;
import com.digitinary.customercare.model.dto.login.RegisterResponseDto;
import com.digitinary.customercare.model.entities.systemuser.SystemUserEntity;
import com.digitinary.customercare.repository.SystemUserRepo;
import com.digitinary.customercare.specification.SpecificationUtils;
import org.springframework.data.jpa.domain.Specification;
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
    public RegisterResponseDto execute(RegisterRequestDto request) {

        Specification<SystemUserEntity> spec =
                SpecificationUtils.<SystemUserEntity>equal("username", request.username())
                        .or(SpecificationUtils.equal("email", request.email()));
        if (systemUserRepo.exists(spec)) {
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

        return new RegisterResponseDto("System user created successfully !",
                "with name :" + systemUser.getUsername(),
                "You must activate tha account");
    }
}
