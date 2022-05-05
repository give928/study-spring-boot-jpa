package com.give928.springboot.jpa.repository.order.query;

import com.give928.springboot.jpa.domain.common.Address;
import com.give928.springboot.jpa.domain.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class OrderFlatDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate; // 주문시간
    private Address address;
    private OrderStatus orderStatus;
    private String itemName; // 상품 명
    private int orderPrice; // 주문 가격
    private int count; // 주문 수량
}
