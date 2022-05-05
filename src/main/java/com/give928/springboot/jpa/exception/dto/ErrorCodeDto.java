package com.give928.springboot.jpa.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ErrorCodeDto {
    private String code;
    private final LocalDateTime date = LocalDateTime.now();
}
