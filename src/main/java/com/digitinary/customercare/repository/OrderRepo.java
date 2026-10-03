package com.digitinary.customercare.repository;

import com.digitinary.customercare.model.entities.customer.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<OrderEntity,Long> , JpaSpecificationExecutor<OrderEntity> {
}
