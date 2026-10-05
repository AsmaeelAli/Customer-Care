package com.digitinary.customercare.controller.customer;

import com.digitinary.customercare.model.dto.api.*;
import com.digitinary.customercare.usecase.customer.DropCustomer;
import com.digitinary.customercare.usecase.customer.GetAllCustomer;
import com.digitinary.customercare.usecase.customer.GetCustomer;
import com.digitinary.customercare.usecase.customer.ModifyCustomer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final GetAllCustomer getAllCustomer;
    private final GetCustomer getCustomer;
    private final ModifyCustomer modifyCustomer;
    private final DropCustomer dropCustomer;

    public CustomerController(GetAllCustomer getAllCustomer, GetCustomer getCustomer, ModifyCustomer modifyCustomer, DropCustomer dropCustomer) {
        this.getAllCustomer = getAllCustomer;
        this.getCustomer = getCustomer;
        this.modifyCustomer = modifyCustomer;
        this.dropCustomer = dropCustomer;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Page<UserResponseDto>> getAllCustomer(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "5") Integer size,
            HttpServletRequest request) {

        ResponseMetaDto meta = new ResponseMetaDto(request.getRequestURI(), HttpStatus.OK.value());

        return new ApiResponse<>(meta, getAllCustomer.execute(pageNumber, size));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<UserResponseDto> getCustomerById(@PathVariable Long id, HttpServletRequest request) {
        ResponseMetaDto meta = new ResponseMetaDto(request.getRequestURI(), HttpStatus.OK.value());
        return new ApiResponse<>(meta, getCustomer.execute(id));
    }

    @PatchMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<UserResponseDto> updateCustomerByUsername(
            @PathVariable String username,
            @Valid @RequestBody UpdateUserRequestDto updateRequest,
            HttpServletRequest request) {

        ResponseMetaDto meta = new ResponseMetaDto(request.getRequestURI(), HttpStatus.OK.value());
        return new ApiResponse<>(meta, modifyCustomer.execute(username, updateRequest));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MessageResponse> deleteCustomerById(@PathVariable Long id, HttpServletRequest request) {
        ResponseMetaDto meta = new ResponseMetaDto(request.getRequestURI(), HttpStatus.OK.value());
        return new ApiResponse<>(meta, dropCustomer.execute(id));
    }
}
