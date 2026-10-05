package com.digitinary.customercare.controller.customer;

import com.digitinary.customercare.model.dto.api.ApiResponse;
import com.digitinary.customercare.model.dto.api.ResponseMetaDto;
import com.digitinary.customercare.model.dto.login.LoginRequest;
import com.digitinary.customercare.model.dto.login.LoginResponse;
import com.digitinary.customercare.model.dto.login.RegisterRequestDto;
import com.digitinary.customercare.model.dto.login.RegisterResponseDto;
import com.digitinary.customercare.security.JwtService;
import com.digitinary.customercare.usecase.customer.CreateCustomer;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public ApiResponse<RegisterResponseDto> registerCustomer(@Valid @RequestBody RegisterRequestDto request, HttpServletRequest httpRequest) {

        RegisterResponseDto reg = createCustomer.execute(request);
        ResponseMetaDto meta = new ResponseMetaDto(httpRequest.getRequestURI(), HttpStatus.CREATED.value());
        return new ApiResponse<>(meta, reg);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK) // 200
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        Authentication authentication = customerAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        UserDetails user = (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(user);

        LoginResponse loginResponse = new LoginResponse(token, "Bearer", jwtService.getExpirationMs());
        ResponseMetaDto meta = new ResponseMetaDto(httpRequest.getRequestURI(), HttpStatus.OK.value());
        return new ApiResponse<>(meta, loginResponse);
    }

}
