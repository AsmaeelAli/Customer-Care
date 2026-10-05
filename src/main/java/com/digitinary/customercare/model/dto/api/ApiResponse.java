package com.digitinary.customercare.model.dto.api;


public record ApiResponse<T>(
        ResponseMetaDto meta,
        T body
) {
}