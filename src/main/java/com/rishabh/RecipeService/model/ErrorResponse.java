package com.rishabh.RecipeService.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@Builder
public class ErrorResponse {

    private String message;
    private String details;
    private OffsetDateTime time;
    private HttpStatus status;
}
