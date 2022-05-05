package com.give928.springboot.jpa.exception.dto;

import lombok.Getter;

@Getter
public class ErrorMessageDto extends ErrorCodeDto {
    private final String message;

    public ErrorMessageDto(String code, String message) {
        super(code);
        this.message = message;
    }
}
