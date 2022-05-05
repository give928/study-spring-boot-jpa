package com.give928.springboot.jpa.domain.common;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
@Builder
@Getter
@ToString
public class Address {
    private String city;
    private String street;
    private String zipcode;

    protected Address() {
    }
}
