package com.give928.springboot.jpa.repository.order.query;

import com.give928.springboot.jpa.domain.common.Address;
import com.give928.springboot.jpa.domain.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class OrderQueryDto {
    private final Long orderId;
    private final String name;
    private final LocalDateTime orderDate; //주문시간
    private final OrderStatus orderStatus;
    private final Address address;
    private List<OrderItemQueryDto> orderItems;

    public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate,
                         OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }

    public void setOrderItems(List<OrderItemQueryDto> orderItems) {
        this.orderItems = orderItems;
    }
}
