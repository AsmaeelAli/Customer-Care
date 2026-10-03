package com.digitinary.customercare.controller.customer;

import com.digitinary.customercare.usecase.customer.CreateCustomer;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CreateCustomer createCustomer;

    public CustomerController(CreateCustomer createCustomer) {
        this.createCustomer = createCustomer;
    }



}