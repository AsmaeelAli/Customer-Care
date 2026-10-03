package com.digitinary.customercare.controller.customer;

import com.digitinary.customercare.model.dto.login.RegisterResponseDto;
import com.digitinary.customercare.model.dto.login.RegisterRequestDto;
import com.digitinary.customercare.model.dto.login.LoginRequest;
import com.digitinary.customercare.model.dto.login.LoginResponse;
import com.digitinary.customercare.security.JwtService;
import com.digitinary.customercare.usecase.customer.CreateCustomer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/customers")
public class CustomerAuthController {
    private final AuthenticationManager customerAuthenticationManager;
    private final JwtService jwtService;
    private final CreateCustomer createCustomer;

    public CustomerAuthController(@Qualifier("customerAuthenticationManager")
                                  AuthenticationManager authenticationManager,
                                  JwtService jwtService,
                                  CreateCustomer createCustomer) {
        this.customerAuthenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.createCustomer = createCustomer;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED) // 201
    public RegisterResponseDto registerCustomer(@Valid @RequestBody RegisterRequestDto request) {
        return createCustomer.execute(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {

        System.out.println("step 1: login request received for username: " + request.username());

        Authentication authentication = customerAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        System.out.println("step 6: authentication.getPrincipal()");
        UserDetails user = (UserDetails) authentication.getPrincipal();

        System.out.println("step 7: generating token");
        String token = jwtService.generateToken(user);

        System.out.println("step 8: returning token");
        return new LoginResponse(token, "Bearer", jwtService.getExpirationMs());
    }

}
