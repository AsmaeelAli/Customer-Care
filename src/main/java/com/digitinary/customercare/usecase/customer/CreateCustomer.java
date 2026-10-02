package com.digitinary.customercare.usecase.customer;

import com.digitinary.customercare.common.enums.Roles;
import com.digitinary.customercare.model.dto.customer.CustomerRequestDto;
import com.digitinary.customercare.model.entities.customer.CustomerEntity;
import com.digitinary.customercare.repository.CustomerRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CreateCustomer {
    private final CustomerRepo customerRepo;


    public CreateCustomer(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public String execute(CustomerRequestDto requestDto) {
        CustomerEntity customer = new CustomerEntity(
                requestDto.name(),
                requestDto.username(),
                requestDto.password(),
                requestDto.email(),
                requestDto.phone(),
                LocalDateTime.now(),
                Roles.USER
        );
        customerRepo.save(customer);
        return "Customer created successfully";
    }
}
