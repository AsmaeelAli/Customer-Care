package com.digitinary.customercare.repository;

import com.digitinary.customercare.model.entities.customer.CustomerEntity;
import com.digitinary.customercare.model.entities.customer.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<OrderEntity, Long>, JpaSpecificationExecutor<OrderEntity> {
    Page<OrderEntity> findByCustomer(CustomerEntity customer, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "items")
    Page<OrderEntity> findAll(Pageable pageable);
}
