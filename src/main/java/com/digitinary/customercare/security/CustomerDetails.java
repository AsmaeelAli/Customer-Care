package com.digitinary.customercare.security;

import com.digitinary.customercare.model.entities.customer.CustomerEntity;
import com.digitinary.customercare.repository.CustomerRepo;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomerDetails implements UserDetailsService {
    private final CustomerRepo customerRepo;

    public CustomerDetails(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {

        CustomerEntity customer = customerRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username [" + username + "] not found"));


        return User.withUsername(customer.getUsername())
                .password(customer.getPassword()) // طبعا هون البروفايدر الخاص فينا بالاخص العميل تحقق من الباسوورد وهون بدنا نمرره
                .roles(customer.getRole().name()) // هنا بنجيب الرول الخاصة بالعميل
                .disabled(!customer.isEnabled()) //  هنا نتحقق منه اذا كان مصرح له الدخول او لا اذا كانت صح ممنوع الدخول !
                .build();
    }
}
