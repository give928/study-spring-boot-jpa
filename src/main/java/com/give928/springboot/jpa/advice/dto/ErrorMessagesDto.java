package com.give928.springboot.jpa.advice.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class ErrorMessagesDto extends ErrorDto {
    private final List<String> messages;

    public ErrorMessagesDto(HttpStatus httpStatus, List<String> messages) {
        super(httpStatus);
        this.messages = messages;
    }
}
