package com.give928.springboot.jpa.api.member.dto;

import com.give928.springboot.jpa.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberResponseDto {
    private final String name;
    private final String city;
    private final String street;
    private final String zipcode;

    public MemberResponseDto(Member m) {
        this.name = m.getName();
        this.city = m.getAddress().getCity();
        this.street = m.getAddress().getStreet();
        this.zipcode = m.getAddress().getZipcode();
    }
}
