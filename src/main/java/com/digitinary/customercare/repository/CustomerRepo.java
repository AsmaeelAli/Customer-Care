package com.digitinary.customercare.repository;

import com.digitinary.customercare.model.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<CustomerEntity, Long> , JpaSpecificationExecutor<CustomerEntity> {
}
