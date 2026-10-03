package com.digitinary.customercare.model.dto.login;


public record RegisterResponseDto(
        String message,
        String username,
        String note
) {
}
