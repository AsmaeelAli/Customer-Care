package com.digitinary.customercare.controller.order;

import com.digitinary.customercare.common.enums.OrderStatus;
import com.digitinary.customercare.model.dto.api.ApiResponse;
import com.digitinary.customercare.model.dto.api.ResponseMetaDto;
import com.digitinary.customercare.model.dto.order.OrderRequestDto;
import com.digitinary.customercare.model.dto.order.OrderResponseDto;
import com.digitinary.customercare.usecase.order.CreateOrder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final CreateOrder createOrder;

    public OrderController(CreateOrder createOrder) {
        this.createOrder = createOrder;
    }

    @PostMapping("/{username}")
    public ApiResponse<OrderResponseDto> createOrder(
            @Valid @RequestBody OrderRequestDto requestDto,
            @PathVariable String username,
            HttpServletRequest request) {

        ResponseMetaDto meta = new ResponseMetaDto(request.getRequestURI(),HttpStatus.CREATED.value());
        return new ApiResponse<>(meta,createOrder.execute(username, requestDto));
    }

    @GetMapping
    public void getAllOrder(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

    }

    @GetMapping("/{id}")
    public void getOrderById(
            @PathVariable Long id) {

    }

    @PatchMapping("/{id}")
    public void patchOrderById(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {

    }

    @DeleteMapping("/{id}")
    public void deleteOrderById(
            @PathVariable Long id) {

    }
}