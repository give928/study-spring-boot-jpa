package com.give928.springboot.jpa.api.member.dto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class UpdateMemberRequestDto {
    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
