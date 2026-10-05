package com.digitinary.customercare.excptions;

import com.digitinary.customercare.model.dto.api.ApiResponse;
import com.digitinary.customercare.model.dto.api.MessageResponse;
import com.digitinary.customercare.model.dto.api.ResponseMetaDto;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 1. Bad Credentials (401 Unauthorized)
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<MessageResponse> handleBadCredentials(BadCredentialsException e, HttpServletRequest request) {
        log.debug("Login failed: {}", e.getMessage());

        ResponseMetaDto meta = new ResponseMetaDto(request.getRequestURI(), HttpStatus.UNAUTHORIZED.value());
        MessageResponse body = new MessageResponse("Invalid username or password");

        return new ApiResponse<>(meta, body);
    }

    //  هذا الاكسبشن هو نفسه الذي يرميه البروفايدر عند التحقق من مستخدم معنون انه disabled
    // 2. Disabled Account (401 Unauthorized)
    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<MessageResponse> handleDisabled(DisabledException e, HttpServletRequest request) {
        log.debug("Account disabled attempt: {}", e.getMessage());

        ResponseMetaDto meta = new ResponseMetaDto(request.getRequestURI(), HttpStatus.UNAUTHORIZED.value());
        MessageResponse body = new MessageResponse("Account is disabled");

        return new ApiResponse<>(meta, body);
    }

    // 3. Illegal Argument (400 Bad Request)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<MessageResponse> handleBadRequest(IllegalArgumentException e, HttpServletRequest request) {
        log.debug("Bad request parameter: {}", e.getMessage());

        ResponseMetaDto meta = new ResponseMetaDto(request.getRequestURI(), HttpStatus.BAD_REQUEST.value());
        MessageResponse body = new MessageResponse(e.getMessage());

        return new ApiResponse<>(meta, body);
    }

    // في حال مثلا ادمن حاول يحذف كستمر وعند اي طلبية فعالة او امور الكستمر نفسها
    // 4. Illegal State / Data Conflict (409 Conflict)
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<MessageResponse> handleResourceConflict(IllegalStateException e, HttpServletRequest request) {
        log.debug("Resource conflict: {}", e.getMessage());

        ResponseMetaDto meta = new ResponseMetaDto(request.getRequestURI(), HttpStatus.CONFLICT.value());
        MessageResponse body = new MessageResponse(e.getMessage());

        return new ApiResponse<>(meta, body);
    }

    // 5. Resource Not Found (404 Not Found)
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<MessageResponse> handleNotFound(NoSuchElementException e, HttpServletRequest request) {
        log.debug("Resource not found: {}", e.getMessage());

        ResponseMetaDto meta = new ResponseMetaDto(request.getRequestURI(), HttpStatus.NOT_FOUND.value());
        MessageResponse body = new MessageResponse(e.getMessage());

        return new ApiResponse<>(meta, body);
    }

    // 6. Validation Errors (400 Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<MessageResponse> handleValidation(MethodArgumentNotValidException e, HttpServletRequest request) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + " " + f.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request");

        log.debug("Validation failed for request [{}]: {}", request.getRequestURI(), msg);

        ResponseMetaDto meta = new ResponseMetaDto(request.getRequestURI(), HttpStatus.BAD_REQUEST.value());
        MessageResponse body = new MessageResponse(msg);

        return new ApiResponse<>(meta, body);
    }

}
