package com.give928.springboot.jpa.api.member.dto;

import com.give928.springboot.jpa.domain.member.Member;
import lombok.Getter;

@Getter
public class UpdateMemberResponseDto {
    private final Long id;
    private final String name;
    private final String city;
    private final String street;
    private final String zipcode;

    public UpdateMemberResponseDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.city = member.getAddress().getCity();
        this.street = member.getAddress().getStreet();
        this.zipcode = member.getAddress().getZipcode();
    }
}
