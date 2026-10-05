package com.digitinary.customercare.model.entities.customer;

import com.digitinary.customercare.common.enums.OrderStatus;
import com.digitinary.customercare.common.id.SnowflakeId;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ORDERS")
public class OrderEntity {

    @Id
    @SnowflakeId
    private long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private CustomerEntity customer;

    @NotBlank
    @Size(min = 1, max = 100)
    @Column(name = "ORDER_NAME", nullable = false, length = 100)
    private String orderName;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemEntity> items = new ArrayList<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private OrderStatus status;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "TOTAL", nullable = false, precision = 19, scale = 2)
    private BigDecimal total;

    @NotNull
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected OrderEntity() {
    }

    public OrderEntity(CustomerEntity customer, String orderName, List<ItemEntity> items, OrderStatus status, BigDecimal total, LocalDateTime createdAt) {
        this.customer = customer;
        this.orderName = orderName;
        this.items = items;
        this.status = status;
        this.total = total;
        this.createdAt = createdAt;

    }

}