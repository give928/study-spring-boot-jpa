package com.give928.springboot.jpa.api.order.dto;

import com.give928.springboot.jpa.domain.order.OrderItem;
import lombok.Getter;

@Getter
public class OrderItemResponseDto {
    private final String itemName; // 상품 명
    private final int orderPrice; // 주문 가격
    private final int count; // 주문 수량

    public OrderItemResponseDto(OrderItem orderItem) {
        this.itemName = orderItem.getItem().getName();
        this.orderPrice = orderItem.getOrderPrice();
        this.count = orderItem.getCount();
    }
}
