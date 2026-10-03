package com.digitinary.customercare.controller.systemuser;

import com.digitinary.customercare.model.dto.MessageResponse;
import com.digitinary.customercare.model.dto.login.RegisterRequestDto;
import com.digitinary.customercare.usecase.systemuser.ChangeRole;
import com.digitinary.customercare.usecase.systemuser.CreateSystemUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/system-users")
public class SystemUserController {
    private final ChangeRole changeRole;
    private final CreateSystemUser createSystemUser;

    public SystemUserController(ChangeRole changeRole, CreateSystemUser createSystemUser) {
        this.changeRole = changeRole;
        this.createSystemUser = createSystemUser;
    }


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse createSystemUser(@Valid @RequestBody RegisterRequestDto requestDto) {

        return new MessageResponse("");
    }
}
