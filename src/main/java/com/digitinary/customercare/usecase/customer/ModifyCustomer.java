package com.digitinary.customercare.usecase.customer;

import com.digitinary.customercare.model.dto.api.UpdateUserRequestDto;
import com.digitinary.customercare.model.dto.api.UserResponseDto;
import com.digitinary.customercare.model.entities.customer.CustomerEntity;
import com.digitinary.customercare.repository.CustomerRepo;
import com.digitinary.customercare.specification.SpecificationUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class ModifyCustomer {
    private final CustomerRepo customerRepo;

    public ModifyCustomer(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Transactional
    @PreAuthorize("#username == authentication.name")
    public UserResponseDto execute(String username, UpdateUserRequestDto request) {

        CustomerEntity customer = customerRepo.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Customer not found"));

        Specification<CustomerEntity> spec =
                SpecificationUtils.<CustomerEntity>equal("email", request.email())
                        .and(SpecificationUtils.notEqual("id", customer.getId()));

        if (customerRepo.exists(spec)) {
            throw new IllegalArgumentException("Email already taken");
        }

        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setPhone(request.phone());

        customerRepo.save(customer);

        return new UserResponseDto(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getRole()
        );
    }
}
