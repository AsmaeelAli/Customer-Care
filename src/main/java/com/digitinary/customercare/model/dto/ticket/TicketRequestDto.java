package com.digitinary.customercare.model.dto.ticket;

import com.digitinary.customercare.common.enums.TicketPriority;
import jakarta.validation.constraints.NotBlank;

public record TicketRequestDto(
        Long customerId,

        @NotBlank
        String subject,

        @NotBlank
        TicketPriority priority
){
}
