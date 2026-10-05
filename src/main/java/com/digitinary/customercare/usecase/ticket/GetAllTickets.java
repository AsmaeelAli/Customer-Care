package com.digitinary.customercare.usecase.ticket;

import com.digitinary.customercare.common.enums.TicketPriority;
import com.digitinary.customercare.common.enums.TicketStatus;
import com.digitinary.customercare.model.dto.ticket.TicketResponseDto;
import com.digitinary.customercare.model.entities.customer.TicketEntity;
import com.digitinary.customercare.repository.TicketRepo;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetAllTickets {

    private final TicketRepo ticketRepo;


    public GetAllTickets(TicketRepo ticketRepo) {
        this.ticketRepo = ticketRepo;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Page<TicketResponseDto> execute(TicketStatus status, TicketPriority priority, Integer pageNumber, Integer size) {

        Pageable page = PageRequest.of(
                pageNumber,
                size,
                Sort.by("createdAt").descending()
        );

        //اولا نقوم ببناء متغير Specification
        //
        // ومن داخله نقوم بانشاء لائحة بريديكت لانها هي التي تحتوي على الكويري الخاصة بنا
        // اي تخصيص نحتاجه نضيفه فقط وبنهاية نقوم بانشاء تخصيص كامل فيه بريديكت
        //
        Specification<TicketEntity> spec = (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (priority != null) {
                predicates.add(cb.equal(root.get("priority"), priority));
            }
            // هنا البريديكت ذهب كا مجموع يعني ممكن الادمن يبحث عن التكت بشكلين اما الحالة او الاهمية او الاثنتين مع بعضهما
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<TicketEntity> tickets = ticketRepo.findAll(spec, page);

        return tickets.map(ticket -> new TicketResponseDto(
                ticket.getId(),
                ticket.getCustomer().getName(),
                ticket.getSubject(),
                ticket.getStatus(),
                ticket.getPriority()
        ));
    }
}