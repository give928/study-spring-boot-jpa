package com.give928.springboot.jpa.advice.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorMessageDto extends ErrorDto {
    private final String message;

    public ErrorMessageDto(HttpStatus httpStatus, String message) {
        super(httpStatus);
        this.message = message;
    }
}
