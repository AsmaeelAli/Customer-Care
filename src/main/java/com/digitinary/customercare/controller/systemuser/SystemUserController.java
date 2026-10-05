package com.digitinary.customercare.controller.systemuser;

import com.digitinary.customercare.model.dto.api.ApiResponse;
import com.digitinary.customercare.model.dto.api.ResponseMetaDto;
import com.digitinary.customercare.model.dto.api.RoleRequestDto;
import com.digitinary.customercare.model.dto.api.UserResponseDto;
import com.digitinary.customercare.usecase.systemuser.ChangeRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


/**
 * اذا ضل وقت عشان اضيف تعديل على اليوزر انتتي مع البزنس تبعها لكن انا فاهمها
 * <p>
 * <p>
 * هي تشبه الكستمر بشكل عام لكن في اختلافات بسيطة
 *
 *
 *
 */

@RestController
@RequestMapping("/api/system-users")
public class SystemUserController {
    private final ChangeRole changeRole;

    public SystemUserController(ChangeRole changeRole) {
        this.changeRole = changeRole;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<UserResponseDto> setRoleById(@PathVariable Long id,
                                                    @Valid @RequestBody RoleRequestDto roleRequest,
                                                    HttpServletRequest request,
                                                    Authentication authentication) {

        ResponseMetaDto meta = new ResponseMetaDto(request.getRequestURI(), HttpStatus.CREATED.value());
        return new ApiResponse<>(meta, changeRole.execute(id, roleRequest, authentication.getName()));
    }
}
