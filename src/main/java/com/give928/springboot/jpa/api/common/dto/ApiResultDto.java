package com.give928.springboot.jpa.api.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiResultDto<T> {
    private int count;
    private T data;
}
