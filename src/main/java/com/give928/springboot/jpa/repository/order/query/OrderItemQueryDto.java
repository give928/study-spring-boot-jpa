package com.give928.springboot.jpa.repository.order.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class OrderItemQueryDto {
    @JsonIgnore
    private Long orderId; // 주문번호
    private String itemName; // 상품 명
    private int orderPrice; // 주문 가격
    private int count; // 주문 수량
}
