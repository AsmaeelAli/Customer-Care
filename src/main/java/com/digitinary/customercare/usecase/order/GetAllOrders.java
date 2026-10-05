package com.digitinary.customercare.usecase.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import com.digitinary.customercare.model.dto.item.ItemResponseDto;
import com.digitinary.customercare.model.dto.order.OrderResponseDto;
import com.digitinary.customercare.model.entities.customer.OrderEntity;
import com.digitinary.customercare.repository.OrderRepo;




import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class GetAllOrders {

    private final OrderRepo orderRepo;

    public GetAllOrders(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }
    @PreAuthorize("hasRole('ADMIN')")
    public Page<OrderResponseDto> execute(Integer pageNumber, Integer size) {

        Pageable page = PageRequest.of(
                pageNumber,
                size,
                Sort.by("createdAt").descending()
        );

        Page<OrderEntity> orders = orderRepo.findAll(page);

        return orders.map(order -> new OrderResponseDto(
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
        ));
    }
}
