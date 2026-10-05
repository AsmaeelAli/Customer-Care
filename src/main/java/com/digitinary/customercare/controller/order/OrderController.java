package com.digitinary.customercare.controller.order;

import com.digitinary.customercare.model.dto.api.ApiResponse;
import com.digitinary.customercare.model.dto.api.MessageResponse;
import com.digitinary.customercare.model.dto.api.ResponseMetaDto;
import com.digitinary.customercare.model.dto.order.OrderRequestDto;
import com.digitinary.customercare.model.dto.order.OrderResponseDto;
import com.digitinary.customercare.usecase.order.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final CreateOrder createOrder;
    private final GetAllOrders getAllOrders;
    private final GetOrderById getOrderById;
    private final DeleteOrder deleteOrder;
    private final GetAllCustomerOrders getAllCustomerOrders;

    public OrderController(
            CreateOrder createOrder,
            GetAllOrders getAllOrders,
            GetOrderById getOrderById,
            DeleteOrder deleteOrder,
            GetAllCustomerOrders getAllCustomerOrders) {

        this.createOrder = createOrder;
        this.getAllOrders = getAllOrders;
        this.getOrderById = getOrderById;
        this.deleteOrder = deleteOrder;
        this.getAllCustomerOrders = getAllCustomerOrders;
    }

    @PostMapping("/{username}")
    public ApiResponse<OrderResponseDto> createOrder(
            @Valid @RequestBody OrderRequestDto requestDto,
            @PathVariable String username,
            HttpServletRequest request) {

        ResponseMetaDto meta = new ResponseMetaDto(request.getRequestURI(), HttpStatus.CREATED.value());
        return new ApiResponse<>(meta, createOrder.execute(username, requestDto));
    }

    @GetMapping
    public ApiResponse<Page<OrderResponseDto>> getAllOrder(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            HttpServletRequest request) {

        ResponseMetaDto meta = new ResponseMetaDto(request.getRequestURI(), HttpStatus.OK.value());
        return new ApiResponse<>(meta, getAllOrders.execute(page, size));
    }

    @GetMapping("/me/{username}")
    public ApiResponse<Page<OrderResponseDto>> getCustomerOrders(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            HttpServletRequest request) {

        ResponseMetaDto meta = new ResponseMetaDto(request.getRequestURI(), HttpStatus.OK.value());
        return new ApiResponse<>(meta, getAllCustomerOrders.execute(username, page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponseDto> getOrderById(
            @PathVariable Long id,
            HttpServletRequest request) {

        ResponseMetaDto meta = new ResponseMetaDto(request.getRequestURI(), HttpStatus.OK.value());
        return new ApiResponse<>(meta, getOrderById.execute(id));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<MessageResponse> deleteOrderById(
            @PathVariable Long id,
            @RequestParam String username,
            HttpServletRequest request) {


        ResponseMetaDto meta = new ResponseMetaDto(request.getRequestURI(), HttpStatus.NO_CONTENT.value());
        return new ApiResponse<>(meta, deleteOrder.execute(username, id));
    }
}
