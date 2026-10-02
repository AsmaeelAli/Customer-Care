package com.digitinary.customercare.usecase.customer;

import com.digitinary.customercare.common.enums.OrderStatus;
import com.digitinary.customercare.common.enums.TicketStatus;
import com.digitinary.customercare.model.dto.customer.CustomerResponseDto;
import com.digitinary.customercare.model.entities.customer.CustomerEntity;
import com.digitinary.customercare.model.entities.customer.OrderEntity;
import com.digitinary.customercare.model.entities.customer.TicketEntity;
import com.digitinary.customercare.repository.CustomerRepo;
import com.digitinary.customercare.repository.OrderRepo;
import com.digitinary.customercare.repository.TicketRepo;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DropCustomer {
    private final CustomerRepo customerRepo;
    private final OrderRepo orderRepo;
    private final TicketRepo ticketRepo;

    public DropCustomer(CustomerRepo customerRepo, OrderRepo orderRepo, TicketRepo ticketRepo) {
        this.customerRepo = customerRepo;
        this.orderRepo = orderRepo;
        this.ticketRepo = ticketRepo;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public String execute(CustomerResponseDto responseDto) {

        Long id = responseDto.id();

        CustomerEntity customer = customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (hasActiveOrder(id) || hasActiveTicket(id)) {
            return "Customer has active orders or tickets";
        }

        customer.setDeletedAt(LocalDateTime.now());
        customerRepo.save(customer);
        return "Customer dropped successfully";
    }

    private boolean hasActiveOrder(Long id) {
        /**
         *   Check if the customer has any active orders (status: SHIPPED or PROCESSING)
         *              مثل ما وصيتنا يا مهندس وليد لا تجيب كل الداتا وتكيش الميموري كاملة استعملو
         *   Specification AND predicate عشان تعمل الفلترة على مستوى الداتا بيز
         *
         *   استعلام عن حالة الاوردر او التكت لو كانت بدونهم كان دمرنا الميموري حرفيا اما بهاي الطريقة
         *   نفسها الداتا بيز بترجع صح او خطا !
         */
        Specification<OrderEntity> spec = (root, query, cb) ->
                cb.and(
                        cb.equal(root.get("customer").get("id"), id),
                        root.get("status").in(List.of(OrderStatus.SHIPPED, OrderStatus.PROCESSING))
                );

        return orderRepo.exists(spec);
    }

    private boolean hasActiveTicket(Long id) {
        Specification<TicketEntity> spec = (root, query, cb) ->
                cb.and(
                        cb.equal(root.get("customer").get("id"), id),
                        root.get("status").in(List.of(TicketStatus.OPEN, TicketStatus.IN_PROGRESS))
                );

        return ticketRepo.exists(spec);
    }
}
