package com.digitinary.customercare.usecase.customer;

import com.digitinary.customercare.common.enums.Roles;
import com.digitinary.customercare.model.dto.login.RegisterRequestDto;
import com.digitinary.customercare.model.dto.login.RegisterResponseDto;
import com.digitinary.customercare.model.entities.customer.CustomerEntity;
import com.digitinary.customercare.repository.CustomerRepo;
import com.digitinary.customercare.specification.SpecificationUtils;
import org.springframework.data.jpa.domain.Specification;
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
    public RegisterResponseDto execute(RegisterRequestDto request) {

        Specification<CustomerEntity> spec =
                SpecificationUtils.<CustomerEntity>equal("username", request.username())
                        .or(SpecificationUtils.equal("email", request.email()));

        if (customerRepo.exists(spec)) {
            throw new IllegalArgumentException("Username or Email already taken");
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
        return new RegisterResponseDto(
                "Customer Created successfully with name :",
                customer.getUsername(),
                "Contact the admin to activate the account.");
    }
}
