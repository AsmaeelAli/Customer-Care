package com.digitinary.customercare.controller.tickete;

import com.digitinary.customercare.common.enums.TicketPriority;
import com.digitinary.customercare.common.enums.TicketStatus;
import com.digitinary.customercare.model.dto.api.ApiResponse;
import com.digitinary.customercare.model.dto.api.ResponseMetaDto;
import com.digitinary.customercare.model.dto.ticket.TicketRequestDto;
import com.digitinary.customercare.model.dto.ticket.TicketResponseDto;
import com.digitinary.customercare.usecase.ticket.ChangeTicketStatus;
import com.digitinary.customercare.usecase.ticket.CreateTicket;
import com.digitinary.customercare.usecase.ticket.GetAllCustomerTickets;
import com.digitinary.customercare.usecase.ticket.GetAllTickets;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final CreateTicket createTicket;
    private final ChangeTicketStatus changeTicketStatus;
    private final GetAllCustomerTickets getAllCustomerTickets;
    private final GetAllTickets getAllTickets;

    public TicketController(CreateTicket createTicket,
                            GetAllCustomerTickets getAllCustomerTickets,
                            GetAllTickets getAllTickets,
                            ChangeTicketStatus changeTicketStatus) {
        this.createTicket = createTicket;
        this.getAllCustomerTickets = getAllCustomerTickets;
        this.getAllTickets = getAllTickets;
        this.changeTicketStatus = changeTicketStatus;
    }

    @PostMapping("/{username}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TicketResponseDto> newTicket(@Valid @RequestBody TicketRequestDto requestDto,
                                                    @PathVariable String username,
                                                    HttpServletRequest request) {

        ResponseMetaDto meta = new ResponseMetaDto(request.getRequestURI(), HttpStatus.CREATED.value());
        return new ApiResponse<>(meta, createTicket.execute(username, requestDto));
    }

    @GetMapping("/{username}")
    public ApiResponse<Page<TicketResponseDto>> getCustomerTickets(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "5") Integer size,
            HttpServletRequest request) {

        ResponseMetaDto meta = new ResponseMetaDto(request.getRequestURI(), HttpStatus.OK.value());
        return new ApiResponse<>(meta,getAllCustomerTickets.execute(username, pageNumber, size)
        );
    }

    @GetMapping
    public ApiResponse<Page<TicketResponseDto>> getAllTickets(
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) TicketPriority priority,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "5") Integer size,
            HttpServletRequest request) {

        ResponseMetaDto meta =
                new ResponseMetaDto(request.getRequestURI(), HttpStatus.OK.value());

        return new ApiResponse<>(
                meta,
                getAllTickets.execute(status, priority, pageNumber, size)
        );
    }


    @PatchMapping("/{ticketId}/status")
    public ApiResponse<TicketResponseDto> changeTicketStatus(
            @PathVariable Long ticketId,
            @RequestParam TicketStatus status,
            HttpServletRequest request) {



        ResponseMetaDto meta =
                new ResponseMetaDto(request.getRequestURI(), HttpStatus.OK.value());

        return new ApiResponse<>(meta, changeTicketStatus.execute(ticketId, status));
    }
}
