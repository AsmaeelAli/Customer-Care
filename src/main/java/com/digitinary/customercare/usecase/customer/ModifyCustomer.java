package com.digitinary.customercare.usecase.customer;

import com.digitinary.customercare.model.entities.customer.CustomerEntity;
import com.digitinary.customercare.repository.CustomerRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModifyCustomer {
    private final CustomerRepo customerRepo;

    public ModifyCustomer(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Transactional
    @PreAuthorize("hasRole('CUSTOMER')")
    public void execute(Long customerId, String newEmail) {
        CustomerEntity customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setEmail(newEmail);
        customerRepo.save(customer);
    }
}
