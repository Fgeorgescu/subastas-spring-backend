package com.subastas.virtual.exception;

import com.subastas.virtual.exception.custom.UnauthorizedException;
import com.subastas.virtual.exception.custom.UserAlreadyExistsException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        ApiError error = new ApiError(HttpStatus.CONFLICT, e.getMessage());
        return buildResponseEntity(error);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorizedException(UnauthorizedException e) {
        ApiError error = new ApiError(HttpStatus.UNAUTHORIZED, e.getMessage());
        return buildResponseEntity(error);
    }

    @ExceptionHandler({Exception.class})
    @Order()
    public ResponseEntity<ApiError> handleUnknownException(Exception e) {
        ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return buildResponseEntity(error);
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(apiError, responseHeaders, apiError.getStatus());
    }
}
