package com.give928.springboot.jpa.api.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class CreateMemberResponseDto {
    private Long id;
}
