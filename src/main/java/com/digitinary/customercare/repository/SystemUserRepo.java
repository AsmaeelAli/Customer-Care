package com.digitinary.customercare.repository;

import com.digitinary.customercare.model.entities.systemuser.SystemUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemUserRepo extends JpaRepository<SystemUserEntity, Long>, JpaSpecificationExecutor<SystemUserEntity> {
    Optional<SystemUserEntity> findByUsername(String username);
}
