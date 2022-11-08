package com.rishabh.RecipeService.exception;

import com.rishabh.RecipeService.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundExceptionHandler(final NotFoundException ex) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .details("Invalid or missing query parameters")
                .status(HttpStatus.BAD_REQUEST)
                .time(OffsetDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(Exception.class)
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
