package com.give928.springboot.jpa.api.order;

import com.give928.springboot.jpa.api.order.dto.OrderSimpleResponseDto;
import com.give928.springboot.jpa.domain.order.Order;
import com.give928.springboot.jpa.repository.order.OrderRepository;
import com.give928.springboot.jpa.repository.order.OrderSearch;
import com.give928.springboot.jpa.repository.order.simplequery.OrderSimpleQueryDto;
import com.give928.springboot.jpa.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/** *
 * xToOne(ManyToOne, OneToOne) 관계 최적화 * Order
 * Order -> Member
 * Order -> Delivery
 *
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /**
     * V1. 엔티티 직접 노출
     * - Hibernate5Module 모듈 등록, LAZY=null 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAllByString(OrderSearch.builder().build());
        for (Order order : orders) {
            order.getMember().getName(); // Lazy 강제 초기화
            order.getDelivery().getAddress(); // Lazy 강제 초기화
        }
        return orders;
    }

    /**
     * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
     * - 단점: 지연로딩으로 쿼리 N번 호출
     */
    @GetMapping("/api/v2/simple-orders")
    public List<OrderSimpleResponseDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(OrderSearch.builder().build());

        return orders.stream()
                .map(OrderSimpleResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O)
     * - fetch join으로 쿼리 1번 호출
     * 참고: fetch join에 대한 자세한 내용은 JPA 기본편 참고(정말 중요함)
     */
    @GetMapping("/api/v3/simple-orders")
    public List<OrderSimpleResponseDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberAndDelivery();

        return orders.stream()
                .map(OrderSimpleResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * V4. JPA에서 DTO로 바로 조회
     * - 쿼리1번 호출
     * - select 절에서 원하는 데이터만 선택해서 조회
     */
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderQueryDtos();
    }
}
