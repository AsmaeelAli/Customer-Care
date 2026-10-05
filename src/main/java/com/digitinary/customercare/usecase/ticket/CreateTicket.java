package com.digitinary.customercare.usecase.ticket;

import com.digitinary.customercare.common.enums.TicketStatus;
import com.digitinary.customercare.model.dto.ticket.TicketRequestDto;
import com.digitinary.customercare.model.dto.ticket.TicketResponseDto;
import com.digitinary.customercare.model.entities.customer.CustomerEntity;
import com.digitinary.customercare.model.entities.customer.TicketEntity;
import com.digitinary.customercare.repository.CustomerRepo;
import com.digitinary.customercare.repository.TicketRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;


@Service
public class CreateTicket {
    private final TicketRepo ticketRepo;
    private final CustomerRepo customerRepo;

    public CreateTicket(TicketRepo ticketRepo, CustomerRepo customerRepo) {
        this.ticketRepo = ticketRepo;
        this.customerRepo = customerRepo;
    }

    @Transactional
    @PreAuthorize("#username == authentication.name or hasRole('Admin')")
    public TicketResponseDto execute(String username, TicketRequestDto requestDto) {

        CustomerEntity customer = customerRepo.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Customer not found"));

        TicketEntity ticket = new TicketEntity(
                customer,
                requestDto.subject(),
                TicketStatus.OPEN,
                requestDto.priority(),
                LocalDateTime.now()

        );

        ticketRepo.save(ticket);
        return new TicketResponseDto(
                ticket.getId(),
                username,
                ticket.getSubject(),
                ticket.getStatus(),
                ticket.getPriority()
        );
    }
}
