package com.digitinary.customercare.model.dto.customer;

import com.digitinary.customercare.common.enums.Roles;

public record CustomerResponseDto(
    Long id,
    String username,
    Roles role
){}
