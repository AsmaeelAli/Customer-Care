package com.digitinary.customercare.model.dto.ticket;

import com.digitinary.customercare.common.enums.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketRequestDto(
        @NotBlank
        String subject,

        @NotNull
        TicketPriority priority
) {
}
