package com.digitinary.customercare.model.dto.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z]{5,30}$", message = "must contain only letters, minimum 5 characters")
        String username,

        @NotBlank
        @Size(min = 8, message = "At least 8 length !")
        String password
) {
}

