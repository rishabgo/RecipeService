package com.rishabh.RecipeService.exception;

import com.rishabh.RecipeService.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.OffsetDateTime;

@ControllerAdvice
public class CustomExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundExceptionHandler(IllegalArgumentException ex) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .details("Invalid or missing query parameters")
                .status(HttpStatus.BAD_REQUEST)
                .time(OffsetDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllException(Exception ex) {

        final ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .details("Something went wrong")
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .time(OffsetDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
