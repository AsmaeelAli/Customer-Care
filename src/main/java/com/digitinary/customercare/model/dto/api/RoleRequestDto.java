package com.digitinary.customercare.model.dto.api;

import com.digitinary.customercare.common.enums.Roles;
import jakarta.validation.constraints.NotNull;

public record RoleRequestDto(
        @NotNull
        Roles role
) {
}
