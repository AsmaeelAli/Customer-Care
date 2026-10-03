package com.digitinary.customercare.usecase.systemuser;

import com.digitinary.customercare.repository.CustomerRepo;
import com.digitinary.customercare.repository.SystemUserRepo;
import org.springframework.stereotype.Service;

@Service
public class ChangeRole {
    private final SystemUserRepo systemUserRepo;
    private final CustomerRepo customerRepo;

    public ChangeRole(SystemUserRepo systemUserRepo, CustomerRepo customerRepo) {
        this.systemUserRepo = systemUserRepo;
        this.customerRepo = customerRepo;
    }

    public String execute(Long id){
        return "";
    }
}
