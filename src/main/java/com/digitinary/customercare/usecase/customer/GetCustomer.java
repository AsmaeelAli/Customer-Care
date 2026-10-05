package com.digitinary.customercare.usecase.customer;

import com.digitinary.customercare.model.dto.api.UserResponseDto;
import com.digitinary.customercare.model.entities.customer.CustomerEntity;
import com.digitinary.customercare.repository.CustomerRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class GetCustomer {
    private final CustomerRepo customerRepo;

    public GetCustomer(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public UserResponseDto execute(Long id) {

        CustomerEntity customer = customerRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Customer not found"));

        return new UserResponseDto(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getRole()
        );
    }
}
