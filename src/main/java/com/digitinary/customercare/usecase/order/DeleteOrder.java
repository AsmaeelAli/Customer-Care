package com.digitinary.customercare.usecase.order;

import com.digitinary.customercare.model.dto.api.MessageResponse;
import com.digitinary.customercare.model.entities.customer.OrderEntity;
import com.digitinary.customercare.repository.OrderRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class DeleteOrder {

    private final OrderRepo orderRepo;

    public DeleteOrder(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    @Transactional
    @PreAuthorize("#username == authentication.name or hasRole('Admin')")
    public MessageResponse execute(String username , Long id) {

        OrderEntity order = orderRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));

        orderRepo.delete(order);

        return new MessageResponse("Order Deleted !");
    }
}
