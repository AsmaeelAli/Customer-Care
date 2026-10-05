package com.digitinary.customercare.security;

import com.digitinary.customercare.model.entities.systemuser.SystemUserEntity;
import com.digitinary.customercare.repository.SystemUserRepo;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SystemUserDetails implements UserDetailsService {

    private final SystemUserRepo systemUserRepo;

    public SystemUserDetails(SystemUserRepo systemUserRepo) {
        this.systemUserRepo = systemUserRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {

        SystemUserEntity systemUser = systemUserRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username [" + username + "] not found"));

        return User.withUsername(systemUser.getUsername())
                .password(systemUser.getPassword())
                .roles(systemUser.getRole().name())// "ADMIN" becomes "ROLE_ADMIN"
                .build();
    }
}
