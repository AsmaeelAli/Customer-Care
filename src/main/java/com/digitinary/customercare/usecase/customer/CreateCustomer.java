package com.digitinary.customercare.usecase.customer;

import com.digitinary.customercare.common.enums.Roles;
import com.digitinary.customercare.model.dto.login.RegisterRequestDto;
import com.digitinary.customercare.model.entities.customer.CustomerEntity;
import com.digitinary.customercare.repository.CustomerRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CreateCustomer {
    private final CustomerRepo customerRepo;
    private final PasswordEncoder passwordEncoder;

    public CreateCustomer(CustomerRepo customerRepo, PasswordEncoder passwordEncoder) {
        this.customerRepo = customerRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String execute(RegisterRequestDto request) {

        if (customerRepo.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already taken");
        }

        CustomerEntity customer = new CustomerEntity(
                request.name(),
                request.username(),
                passwordEncoder.encode(request.password()),
                request.email(),
                request.phone(),
                LocalDateTime.now(),
                Roles.USER
        );

        /**
         * فكرة اني ضفت رول اسمها يوزر هي بختصار ممكن عميل يعمل حساب اله لكن برول مستخدم فقط
         * فما بقدرش يعمل اي اشي ضمن السستم الما الادمن يتاكد منه ويغير الرول الخاصة فيه الى CUSTOMER
         *
         */

        customerRepo.save(customer);
        return "Customer created successfully";
    }
}
