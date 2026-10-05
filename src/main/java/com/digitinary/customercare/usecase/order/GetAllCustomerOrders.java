package com.digitinary.customercare.usecase.order;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import com.digitinary.customercare.model.dto.item.ItemResponseDto;
import com.digitinary.customercare.model.dto.order.OrderResponseDto;
import com.digitinary.customercare.model.entities.customer.CustomerEntity;
import com.digitinary.customercare.model.entities.customer.OrderEntity;
import com.digitinary.customercare.repository.CustomerRepo;
import com.digitinary.customercare.repository.OrderRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
public class GetAllCustomerOrders {

    private final OrderRepo orderRepo;
    private final CustomerRepo customerRepo;

    public GetAllCustomerOrders(OrderRepo orderRepo, CustomerRepo customerRepo) {
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
    }

    @PreAuthorize("#username == authentication.name or hasRole('Admin')")
    public Page<OrderResponseDto> execute(String username, Integer pageNumber, Integer size) {

        CustomerEntity customer = customerRepo.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Customer not found"));

        Pageable page = PageRequest.of(
                pageNumber,
                size,
                Sort.by("createdAt").descending()
        );

        Page<OrderEntity> orders = orderRepo.findByCustomer(customer, page);

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
