package com.digitinary.customercare.excptions;

import com.digitinary.customercare.config.AdminSeeder;
import com.digitinary.customercare.model.dto.MessageResponse;
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

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)// 401
    public MessageResponse badCredentials(BadCredentialsException e) {
        log.debug("Login failed: {}", e.getMessage());
        return new MessageResponse("Invalid username or password");
    }


    // هذا الاكسبشن هو نفسه الذي يرميه البروفايدر عند التحقق من مستخدم معنون انه disabled
    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)// 401
    public MessageResponse disabled() {
        return new MessageResponse("Account is disabled");
    }


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)// 400
    public MessageResponse badRequest(IllegalArgumentException e) {
        return new MessageResponse(e.getMessage());
    }


    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)// 409 تعارض بيانات او عملية داخل النظام
    public MessageResponse resourceConflictException(IllegalStateException e) {
        return new MessageResponse(e.getMessage());
    }


    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)// 404
    public MessageResponse notFound(NoSuchElementException e) {
        return new MessageResponse(e.getMessage());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)// 400
    public MessageResponse validation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + " " + f.getDefaultMessage())
                .findFirst().orElse("Invalid request");
        return new MessageResponse(msg);
    }

}
