package com.digitinary.customercare.model.dto.order;

import com.digitinary.customercare.model.dto.item.ItemRequestDto;

import java.util.List;

public record OrderRequestDto(
        Long customerId,
        List<ItemRequestDto> items
) {}
