package com.digitinary.customercare.model.entities.customer;

import com.digitinary.customercare.common.enums.Roles;
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
@Table(name = "CUSTOMERS")
public class CustomerEntity {

    @Id
    @SnowflakeId
    @Column(name = "ID")
    private long id;

    @NotBlank
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @NotBlank
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @NotBlank
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(name = "PHONE", nullable = false)
    private String phone;

    @NotNull
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "LAST_LOGIN")
    private LocalDateTime lastLogIn;

    @Column(name = "DELETED_AT")
    private LocalDateTime deletedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private Roles role;

    protected CustomerEntity() {
    }

    public CustomerEntity(String name, String username, String password, String email, String phone, LocalDateTime createdAt, Roles role) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
        this.role = role;
        this.deletedAt = null;
        this.lastLogIn = null;
    }

    /**
     * في حال كان العميل معموله دروب وهاي القيمة فيها توقيت فعلي ولا تساوي   null
     * هون بنقدر نساله بشكل سريع هل هو فعال او لا وبنقدر نلغي طلبه قبل ما تعمله token
    */

    public boolean isEnabled() {
        return this.role != Roles.USER;
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}



