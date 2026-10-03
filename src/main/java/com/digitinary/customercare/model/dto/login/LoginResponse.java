package com.digitinary.customercare.model.dto.login;

public record LoginResponse(String token, String type, long expiresInMs) {
}
