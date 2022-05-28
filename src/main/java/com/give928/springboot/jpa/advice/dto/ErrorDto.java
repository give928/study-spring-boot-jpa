package com.give928.springboot.jpa.advice.dto;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ErrorDto {
    private final HttpStatus httpStatus;
    private final LocalDateTime date = LocalDateTime.now();

    public int getStatusCode() {
        return httpStatus.value();
    }

    public String getReasonPhrase() {
        return httpStatus.getReasonPhrase();
    }

    public LocalDateTime getDate() {
        return date;
    }
}
