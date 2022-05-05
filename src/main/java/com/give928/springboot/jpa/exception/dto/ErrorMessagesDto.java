package com.give928.springboot.jpa.exception.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ErrorMessagesDto extends ErrorCodeDto {
    private final List<String> messages;

    public ErrorMessagesDto(String code, List<String> messages) {
        super(code);
        this.messages = messages;
    }
}
