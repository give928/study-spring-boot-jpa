package com.give928.springboot.jpa.repository.order.simplequery;

import com.give928.springboot.jpa.domain.common.Address;
import com.give928.springboot.jpa.domain.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class OrderSimpleQueryDto {
    private final Long orderId;
    private final String name;
    private final LocalDateTime orderDate; //주문시간
    private final OrderStatus orderStatus;
    private final Address address;
}
