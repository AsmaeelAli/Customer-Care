package com.digitinary.customercare.model.dto.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateUserRequestDto(
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z\\u0600-\\u06FF ]{3,30}$", message = "Name should only contain letters")
        String name,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Pattern(regexp = "^\\+?[0-9]{10,14}$", message = "Phone number should be valid")
        String phone
) {
}
