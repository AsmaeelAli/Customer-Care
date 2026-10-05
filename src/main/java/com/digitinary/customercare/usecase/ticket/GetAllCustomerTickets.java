package com.digitinary.customercare.usecase.ticket;

import com.digitinary.customercare.model.dto.ticket.TicketResponseDto;
import com.digitinary.customercare.model.entities.customer.CustomerEntity;
import com.digitinary.customercare.model.entities.customer.TicketEntity;
import com.digitinary.customercare.repository.CustomerRepo;
import com.digitinary.customercare.repository.TicketRepo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.NoSuchElementException;

@Service
public class GetAllCustomerTickets {

    private final TicketRepo ticketRepo;
    private final CustomerRepo customerRepo;

    public GetAllCustomerTickets(TicketRepo ticketRepo, CustomerRepo customerRepo) {
        this.ticketRepo = ticketRepo;
        this.customerRepo = customerRepo;
    }

    public Page<TicketResponseDto> execute(String username, Integer pageNumber, Integer size) {

        CustomerEntity customer = customerRepo.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Customer not found"));

        Pageable page = PageRequest.of(
                pageNumber,
                size,
                Sort.by("createdAt").descending()
        );

        Page<TicketEntity> tickets = ticketRepo.findByCustomer(customer, page);

        return tickets.map(ticket -> new TicketResponseDto(
                ticket.getId(),
                customer.getName(),
                ticket.getSubject(),
                ticket.getStatus(),
                ticket.getPriority()
        ));
    }
}
