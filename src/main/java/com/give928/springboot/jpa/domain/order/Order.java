package com.give928.springboot.jpa.domain.order;

import com.give928.springboot.jpa.domain.delivery.Delivery;
import com.give928.springboot.jpa.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 주문 회원

//    @BatchSize(size = 100) // 지연 로딩 성능 최적화 - 컬렉션이나, 프록시 객체를 한꺼번에 설정한 size 만큼 IN 쿼리로 조회
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; // 배송정보

    private LocalDateTime orderDate; // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    public void changeStatus(OrderStatus status) {
        this.status = status;
    }

    //==연관관계 메서드==//
    /*public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }*/

    //==생성 메서드==//
    /**
     * 주문 생성
     */
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .orderItems(Arrays.asList(orderItems))
//                .orderItems(new ArrayList<>())
                .status(OrderStatus.ORDER)
                .orderDate(LocalDateTime.now())
                .build();

//        order.setMember(member);
        member.getOrders().add(order);

//        order.setDelivery(delivery);
        delivery.setOrder(order);

//        Arrays.asList(orderItems)
//                .forEach(order::addOrderItem);
        Arrays.asList(orderItems).forEach(orderItem -> orderItem.setOrder(order));

        return order;
    }

    //==비지니스 로직==//
    /**
     * 주문 취소
     */
    public void cancel() {
        if (getDelivery().isComp()) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.changeStatus(OrderStatus.CANCEL);

        getOrderItems().forEach(OrderItem::cancel);
    }

    //==조회 로직==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        return getOrderItems().stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }
}
