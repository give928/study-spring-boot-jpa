package com.give928.springboot.jpa.advice;

import com.give928.springboot.jpa.exception.dto.ErrorCodeDto;
import com.give928.springboot.jpa.exception.dto.ErrorMessageDto;
import com.give928.springboot.jpa.exception.dto.ErrorMessagesDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(basePackages = {"com.give928.spring.jpa.api"})
public class ApiExceptionAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorCodeDto handleBindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> allErrors = bindingResult.getAllErrors();

        if (allErrors.size() == 1) {
            return new ErrorMessageDto("BAD_REQUEST", allErrors.get(0).getDefaultMessage());
        }

        List<String> messages = allErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return new ErrorMessagesDto("BAD_REQUEST", messages);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ErrorCodeDto handleRuntimeException(RuntimeException e) {
        if (StringUtils.hasText(e.getMessage())) {
            log.error("[handleRuntimeException]", e);
            return new ErrorMessageDto("BAD_REQUEST", e.getMessage());
        }

        return handleException(e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorCodeDto handleException(Exception e) {
        log.error("[handleException]", e);
        return new ErrorMessageDto("INTERNAL_SERVER_ERROR", "서버 내부 오류");
    }
}
