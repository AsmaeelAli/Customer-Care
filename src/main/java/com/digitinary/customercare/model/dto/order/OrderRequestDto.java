package com.digitinary.customercare.model.dto.order;

import com.digitinary.customercare.model.dto.item.ItemRequestDto;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record OrderRequestDto(
        @NotBlank
        String orderName,
        List<ItemRequestDto> items
) {
}
