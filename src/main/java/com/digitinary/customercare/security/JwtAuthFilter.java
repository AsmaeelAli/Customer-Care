package com.digitinary.customercare.security;

import com.digitinary.customercare.common.enums.Roles;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtService jwtService;
    private final UserDetailsService customerDetails;
    private final UserDetailsService systemUserDetails;

    public JwtAuthFilter(@Qualifier("customerDetails") UserDetailsService customerDetails,
                         @Qualifier("systemUserDetails") UserDetailsService systemUserDetails,
                         JwtService jwtService) {
        this.jwtService = jwtService;
        this.customerDetails = customerDetails;
        this.systemUserDetails = systemUserDetails;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            String username = jwtService.extractUsername(token);
            Roles role = jwtService.extractRole(token);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                String requestURI = request.getRequestURI();


                if ((requestURI.startsWith("/api/system-users") || requestURI.startsWith("/auth/system-users"))
                        && role != Roles.ADMIN) {
                    chain.doFilter(request, response);
                    return;
                }


                UserDetailsService targetService = switch (role) {
                    case ADMIN -> systemUserDetails;
                    case CUSTOMER, USER -> customerDetails;
                };

                UserDetails user = targetService.loadUserByUsername(username);

                if (user.isEnabled()) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }
        } catch (JwtException | UsernameNotFoundException e) {
            log.debug("Rejected JWT: {}", e.getMessage());
        }

        chain.doFilter(request, response);
    }
}