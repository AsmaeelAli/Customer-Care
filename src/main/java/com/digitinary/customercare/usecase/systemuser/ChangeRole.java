package com.digitinary.customercare.usecase.systemuser;

import com.digitinary.customercare.common.enums.Roles;
import com.digitinary.customercare.model.dto.api.MessageResponse;
import com.digitinary.customercare.model.dto.api.RoleRequestDto;
import com.digitinary.customercare.model.dto.api.UserResponseDto;
import com.digitinary.customercare.model.entities.customer.CustomerEntity;
import com.digitinary.customercare.model.entities.systemuser.SystemUserEntity;
import com.digitinary.customercare.repository.CustomerRepo;
import com.digitinary.customercare.repository.SystemUserRepo;
import com.digitinary.customercare.specification.SpecificationUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ChangeRole {

    private final SystemUserRepo systemUserRepo;
    private final CustomerRepo customerRepo;

    public ChangeRole(SystemUserRepo systemUserRepo, CustomerRepo customerRepo) {
        this.systemUserRepo = systemUserRepo;
        this.customerRepo = customerRepo;
    }

    @Transactional
    @PreAuthorize("#username == authentication.name")
    public UserResponseDto execute(Long id, RoleRequestDto roleRequest, String username) {

        List<Roles> systemUserRoles = List.of(Roles.ADMIN, Roles.USER);

        Specification<SystemUserEntity> systemUserSpec =
                SpecificationUtils.<SystemUserEntity>equal("id", id)
                        .and(SpecificationUtils.in("role", systemUserRoles));

        Optional<SystemUserEntity> systemUser = systemUserRepo.findOne(systemUserSpec);

        if (systemUser.isPresent()) {
            SystemUserEntity entity = systemUser.get();

            if (entity.getUsername().equals(username)) {
                throw new IllegalArgumentException("Admin cannot change their own role");
            }

            entity.setRole(roleRequest.role());
            systemUserRepo.save(entity);

            return new UserResponseDto(
                    entity.getId(),
                    entity.getName(),
                    entity.getEmail(),
                    entity.getPhone(),
                    entity.getRole()
            );
        }

        List<Roles> customerRoles = List.of(Roles.USER , Roles.CUSTOMER);

        Specification<CustomerEntity> customerSpec =
                SpecificationUtils.<CustomerEntity>equal("id" , id)
                        .and(SpecificationUtils.in("role", customerRoles));

        Optional<CustomerEntity> customer = customerRepo.findOne(customerSpec);

        if (customer.isPresent()) {

            CustomerEntity entity = customer.get();

            if (roleRequest.role() == Roles.ADMIN) {
                throw new IllegalArgumentException("Customer cannot have ADMIN role");
            }

            entity.setRole(roleRequest.role());
            customerRepo.save(entity);

            return new UserResponseDto(
                    entity.getId(),
                    entity.getName(),
                    entity.getEmail(),
                    entity.getPhone(),
                    entity.getRole()
            );
        }

        throw new NoSuchElementException("User not found with ID: " + id);
    }
}