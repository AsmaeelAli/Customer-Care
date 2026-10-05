package com.digitinary.customercare.model.dto.api;

// في المشاريع حرفيا لا نرجع id الخاصة لمستخدم الا لاسباب معينة مثلا نعدل عليه او نحذفه
public record UserResponseDto(
        Long id,
        String name,
        String email,
        String phone
){
}
