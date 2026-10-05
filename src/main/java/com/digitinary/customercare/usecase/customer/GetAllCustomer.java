package com.digitinary.customercare.usecase.customer;

import com.digitinary.customercare.model.dto.api.UserResponseDto;
import com.digitinary.customercare.model.entities.customer.CustomerEntity;
import com.digitinary.customercare.repository.CustomerRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class GetAllCustomer{
    private final CustomerRepo customerRepo;

    public GetAllCustomer(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public Page<UserResponseDto> execute(Integer pageNumber, Integer size) {

        Pageable page = PageRequest.of(pageNumber,size,Sort.by("createdAt").descending());

        Page<CustomerEntity> customers = customerRepo.findAll(page);

        return customers.map(customer -> new UserResponseDto(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone()
        ));

    }

}
