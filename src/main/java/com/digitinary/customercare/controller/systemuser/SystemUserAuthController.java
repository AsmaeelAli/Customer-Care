package com.digitinary.customercare.controller.systemuser;

import com.digitinary.customercare.model.dto.login.LoginRequest;
import com.digitinary.customercare.model.dto.login.LoginResponse;
import com.digitinary.customercare.model.dto.login.RegisterRequestDto;
import com.digitinary.customercare.model.dto.login.RegisterResponseDto;
import com.digitinary.customercare.security.JwtService;
import com.digitinary.customercare.usecase.systemuser.CreateSystemUser;
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

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponseDto registerSystemUser(@Valid @RequestBody RegisterRequestDto request) {
        return createSystemUser.execute(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request){

        Authentication authentication = systemUserAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(),request.password()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(token , "Bearer" , jwtService.getExpirationMs());
    }

}
