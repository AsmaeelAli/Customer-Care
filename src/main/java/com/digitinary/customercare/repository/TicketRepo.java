package com.digitinary.customercare.repository;

import com.digitinary.customercare.model.entities.customer.CustomerEntity;
import com.digitinary.customercare.model.entities.customer.TicketEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepo extends JpaRepository<TicketEntity,Long> , JpaSpecificationExecutor<TicketEntity> {
    Page<TicketEntity> findByCustomer(CustomerEntity customer, Pageable pageable);
}

