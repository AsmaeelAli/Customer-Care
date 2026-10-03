package com.digitinary.customercare.model.dto.item;

import java.math.BigDecimal;

public record ItemResponseDto(
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal totalItemPrice
) {
}
