package com.digitinary.customercare.usecase.ticket;

import com.digitinary.customercare.common.enums.TicketStatus;
import com.digitinary.customercare.model.dto.ticket.TicketResponseDto;
import com.digitinary.customercare.model.entities.customer.TicketEntity;
import com.digitinary.customercare.repository.TicketRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class ChangeTicketStatus {

    private final TicketRepo ticketRepo;

    public ChangeTicketStatus(TicketRepo ticketRepo) {
        this.ticketRepo = ticketRepo;
    }

    @Transactional
    public TicketResponseDto execute(Long ticketId, TicketStatus newStatus) {

        TicketEntity ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new NoSuchElementException("Ticket not found"));

        TicketStatus currentStatus = ticket.getStatus();

        if (!isAllowed(currentStatus, newStatus)) {
            throw new IllegalStateException(
                    "Cannot change ticket status from " +
                            currentStatus + " to " + newStatus
            );
        }

        ticket.setStatus(newStatus);
        ticketRepo.save(ticket);
        return new TicketResponseDto(
                ticket.getId(),
                ticket.getCustomer().getUsername(),
                ticket.getSubject(),
                ticket.getStatus(),
                ticket.getPriority()
        );
    }

    private boolean isAllowed(TicketStatus current, TicketStatus next) {

        return switch (current) {
            case OPEN -> next == TicketStatus.IN_PROGRESS;
            case IN_PROGRESS -> next == TicketStatus.RESOLVED;
            case RESOLVED -> next == TicketStatus.CLOSED;
            case CLOSED -> false;
        };
    }
}
