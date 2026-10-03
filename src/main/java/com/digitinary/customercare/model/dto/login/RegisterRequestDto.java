package com.digitinary.customercare.model.dto.login;

import jakarta.validation.constraints.*;

public record RegisterRequestDto(
        @NotEmpty
        @Pattern(regexp = "^[a-zA-Z\\u0600-\\u06FF ]{3,30}$", message = "must contain only letters, minimum 3 characters")
        String name,

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z]{5,30}$", message = "must contain only letters, minimum 5 characters")
        String username,

        @NotBlank
        @Size(min = 8, message = "At least 8 length !")
        String password,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Pattern(regexp = "^\\+?[0-9]{10,14}$", message = "number should be valid")
        String phone
){}
