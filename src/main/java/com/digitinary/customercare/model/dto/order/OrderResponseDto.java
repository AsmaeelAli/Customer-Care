package com.digitinary.customercare.model.dto.order;

import com.digitinary.customercare.common.enums.OrderStatus;
import com.digitinary.customercare.model.dto.item.ItemResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        OrderStatus orderStatus,
        LocalDateTime createdAt,
        List<ItemResponseDto> items
){
}
