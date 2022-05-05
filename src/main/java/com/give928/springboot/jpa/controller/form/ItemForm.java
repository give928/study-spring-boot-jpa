package com.give928.springboot.jpa.controller.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Builder
@Getter
public class ItemForm {
    private Long id;

    @NotEmpty(message = "책 이름은 필수 입니다")
    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;
}
