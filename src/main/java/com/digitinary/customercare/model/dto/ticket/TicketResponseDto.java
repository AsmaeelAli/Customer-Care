package com.digitinary.customercare.model.dto.ticket;

import com.digitinary.customercare.common.enums.TicketPriority;
import com.digitinary.customercare.common.enums.TicketStatus;

// نفس الاشي هون لازم  كا بزنس يا نعلم على id او نضلله في كل رد للكستمر براي انا
public record TicketResponseDto(
        Long ticketId,
        String customerName,
        String subject,
        TicketStatus status,
        TicketPriority priority
) {
}
