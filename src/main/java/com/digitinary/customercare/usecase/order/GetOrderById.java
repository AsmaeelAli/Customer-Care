package com.digitinary.customercare.usecase.order;

import com.digitinary.customercare.model.dto.item.ItemResponseDto;
import com.digitinary.customercare.model.dto.order.OrderResponseDto;
import com.digitinary.customercare.model.entities.customer.OrderEntity;
import com.digitinary.customercare.repository.OrderRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
public class GetOrderById {

    private final OrderRepo orderRepo;

    public GetOrderById(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponseDto execute(Long id) {

        OrderEntity order = orderRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));

        return new OrderResponseDto(
                order.getId(),
                order.getOrderName(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getItems().stream()
                        .map(item -> new ItemResponseDto(
                                item.getProductName(),
                                item.getQuantity(),
                                item.getUnitPrice(),
                                item.getUnitPrice()
                                        .multiply(BigDecimal.valueOf(item.getQuantity()))
                        ))
                        .toList()
        );
    }
}