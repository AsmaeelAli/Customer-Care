package com.digitinary.customercare.usecase.systemuser;

import com.digitinary.customercare.common.enums.Roles;
import com.digitinary.customercare.model.dto.MessageResponse;
import com.digitinary.customercare.model.entities.customer.CustomerEntity;
import com.digitinary.customercare.model.entities.systemuser.SystemUserEntity;
import com.digitinary.customercare.repository.CustomerRepo;
import com.digitinary.customercare.repository.SystemUserRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @PreAuthorize("hasRole('ADMIN')")
    public MessageResponse execute(Long id, Roles role) {

        Optional<CustomerEntity> customer = customerRepo.findById(id);
        if (customer.isPresent()) {
            customer.get().setRole(role);
            return new MessageResponse("Role updated successfully for customer");
        }

        Optional<SystemUserEntity> systemUser = systemUserRepo.findById(id);
        if (systemUser.isPresent()) {
            systemUser.get().setRole(role);
            return new MessageResponse("Role updated successfully for system user");
        }

        throw new NoSuchElementException("User not found with ID: " + id);
    }
}
