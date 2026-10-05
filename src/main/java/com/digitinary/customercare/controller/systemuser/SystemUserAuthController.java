package com.digitinary.customercare.controller.systemuser;

import com.digitinary.customercare.model.dto.api.ApiResponse;
import com.digitinary.customercare.model.dto.api.ResponseMetaDto;
import com.digitinary.customercare.model.dto.login.LoginRequest;
import com.digitinary.customercare.model.dto.login.LoginResponse;
import com.digitinary.customercare.model.dto.login.RegisterRequestDto;
import com.digitinary.customercare.model.dto.login.RegisterResponseDto;
import com.digitinary.customercare.security.JwtService;
import com.digitinary.customercare.usecase.systemuser.CreateSystemUser;
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
@RequestMapping("/auth/system-users")
public class SystemUserAuthController {

    private final AuthenticationManager systemUserAuthenticationManager;
    private final JwtService jwtService;
    private final CreateSystemUser createSystemUser;

    public SystemUserAuthController(@Qualifier("systemUserAuthenticationManager")
                                    AuthenticationManager authenticationManager,
                                    JwtService jwtService,
                                    CreateSystemUser createSystemUser) {
        this.systemUserAuthenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.createSystemUser = createSystemUser;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public ApiResponse<RegisterResponseDto> registerSystemUser(@Valid @RequestBody RegisterRequestDto request, HttpServletRequest httpRequest) {

        RegisterResponseDto reg = createSystemUser.execute(request);
        ResponseMetaDto meta = new ResponseMetaDto(httpRequest.getRequestURI(), HttpStatus.CREATED.value());

        return new ApiResponse<>(meta, reg);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK) // 200
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        Authentication authentication = systemUserAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        LoginResponse loginResponse = new LoginResponse(token, "Bearer", jwtService.getExpirationMs());
        ResponseMetaDto meta = new ResponseMetaDto(httpRequest.getRequestURI(), HttpStatus.OK.value());

        return new ApiResponse<>(meta, loginResponse);
    }

}