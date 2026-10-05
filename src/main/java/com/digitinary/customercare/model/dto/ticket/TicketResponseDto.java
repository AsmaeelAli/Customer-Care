package com.digitinary.customercare.model.dto.ticket;

import com.digitinary.customercare.common.enums.TicketPriority;
import com.digitinary.customercare.common.enums.TicketStatus;

public record TicketResponseDto(
        String customerName,
        String subject,
        TicketStatus status,
        TicketPriority priority
){
}
