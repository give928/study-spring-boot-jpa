package com.give928.springboot.jpa.api.order.dto;

import com.give928.springboot.jpa.domain.common.Address;
import com.give928.springboot.jpa.domain.order.Order;
import com.give928.springboot.jpa.domain.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class OrderSimpleResponseDto {
    private final Long orderId;
    private final String name;
    private final LocalDateTime orderDate; //주문시간
    private final OrderStatus orderStatus;
    private final Address address;

    public OrderSimpleResponseDto(Order order) {
        this.orderId = order.getId();
        this.name = order.getMember().getName(); // LAZY 초기화
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getStatus();
        this.address = order.getDelivery().getAddress(); // LAZY 초기화
    }
}
