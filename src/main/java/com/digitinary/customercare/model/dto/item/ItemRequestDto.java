package com.digitinary.customercare.model.dto.item;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ItemRequestDto(
        @NotBlank
        @Size(min = 1, max = 100)
        String productName,

        @NotNull
        @DecimalMin(value = "1", message = "Quantity must be greater than or equal to 1")
        Integer quantity,

        @NotNull
        @DecimalMin(value = "0.0",message = "Unit price must be greater than or equal to 0.0")
        @Digits(integer = 17, fraction = 2)
        BigDecimal unitPrice
) {
}
