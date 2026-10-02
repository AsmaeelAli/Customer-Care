package com.digitinary.customercare.model.dto.customer;

import jakarta.validation.constraints.*;

public record CustomerRequestDto(
        @NotEmpty
        @Pattern(regexp = "^[a-zA-Z\\u0600-\\u06FF ]{3,30}$", message = "Name should only contain letters")
        String name,

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z]{5,30}$", message = "Username should only contain letters")
        String username,

        @NotBlank
        @Size(min = 8)
        String password,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Pattern(regexp = "^\\+?[0-9]{10,14}$", message = "Phone number should be valid")
        String phone
){}
