package com.digitinary.customercare.usecase.order;

import com.digitinary.customercare.common.enums.OrderStatus;
import com.digitinary.customercare.model.dto.item.ItemRequestDto;
import com.digitinary.customercare.model.dto.item.ItemResponseDto;
import com.digitinary.customercare.model.dto.order.OrderRequestDto;
import com.digitinary.customercare.model.dto.order.OrderResponseDto;
import com.digitinary.customercare.model.entities.customer.CustomerEntity;
import com.digitinary.customercare.model.entities.customer.ItemEntity;
import com.digitinary.customercare.model.entities.customer.OrderEntity;
import com.digitinary.customercare.repository.CustomerRepo;
import com.digitinary.customercare.repository.OrderRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class CreateOrder {
    private final CustomerRepo customerRepo;
    private final OrderRepo orderRepo;

    public CreateOrder(CustomerRepo customerRepo, OrderRepo orderRepo) {
        this.customerRepo = customerRepo;
        this.orderRepo = orderRepo;
    }

    @Transactional
    public OrderResponseDto execute(OrderRequestDto requestDto) {

        CustomerEntity customer = customerRepo.findById(requestDto.customerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        OrderEntity order = new OrderEntity(
                customer,
                new ArrayList<>(),
                OrderStatus.PENDING,
                BigDecimal.valueOf(0.0),
                LocalDateTime.now()
        );

        BigDecimal totalAmount = BigDecimal.ZERO;

        if(requestDto.items() != null && !requestDto.items().isEmpty()) {
            for (ItemRequestDto items : requestDto.items()) {
                ItemEntity item = new ItemEntity(order, items.productName(), items.quantity(), items.unitPrice());
                order.getItems().add(item);

                BigDecimal itemTotal = items.unitPrice().multiply(BigDecimal.valueOf(items.quantity()));
                totalAmount = totalAmount.add(itemTotal);
            }
        }

        order.setTotal(totalAmount);

        orderRepo.save(order);

        return new OrderResponseDto(
                order.getStatus(),
                order.getCreatedAt(),
                order.getItems().stream()
                        .map(item -> new ItemResponseDto(
                                item.getProductName(),
                                item.getQuantity(),
                                item.getUnitPrice(),
                                item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                        ))
                        .toList()
        );
    }
}
