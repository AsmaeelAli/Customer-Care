package com.digitinary.customercare.repository;

import com.digitinary.customercare.model.entities.customer.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepo extends JpaRepository<TicketEntity,Long> , JpaSpecificationExecutor<TicketEntity> {

}

