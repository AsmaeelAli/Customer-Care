package com.digitinary.customercare.model.entities.customer;

import com.digitinary.customercare.common.enums.TicketPriority;
import com.digitinary.customercare.common.enums.TicketStatus;
import com.digitinary.customercare.common.id.SnowflakeId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "TICKETS")
public class TicketEntity {

    @Id
    @SnowflakeId
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private CustomerEntity customer;

    @NotBlank
    @Column(name = "SUBJECT", nullable = false)
    private String subject;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private TicketStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "PRIORITY", nullable = false)
    private TicketPriority priority;

    @NotNull
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    protected TicketEntity() {
    }

    public TicketEntity(CustomerEntity customer, String subject, TicketStatus status, TicketPriority priority, LocalDateTime createdAt) {
        this.customer = customer;
        this.subject = subject;
        this.status = status;
        this.priority = priority;
        this.createdAt = createdAt;
    }
}