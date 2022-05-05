package com.give928.springboot.jpa.api.order;

import com.give928.springboot.jpa.api.order.dto.OrderResponseDto;
import com.give928.springboot.jpa.domain.order.Order;
import com.give928.springboot.jpa.domain.order.OrderItem;
import com.give928.springboot.jpa.repository.order.OrderRepository;
import com.give928.springboot.jpa.repository.order.OrderSearch;
import com.give928.springboot.jpa.repository.order.query.OrderFlatDto;
import com.give928.springboot.jpa.repository.order.query.OrderItemQueryDto;
import com.give928.springboot.jpa.repository.order.query.OrderQueryDto;
import com.give928.springboot.jpa.repository.order.query.OrderQueryRepository;
import com.give928.springboot.jpa.service.order.query.OrderQueryService;
import com.give928.springboot.jpa.service.order.query.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * V1. 엔티티 직접 노출
 * - 엔티티가 변하면 API 스펙이 변한다.
 * - 트랜잭션 안에서 지연 로딩 필요
 * - 양방향 연관관계 문제
 * <p>
 * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
 * - 트랜잭션 안에서 지연 로딩 필요
 * <p>
 * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O)
 * - 페이징 시에는 N 부분을 포기해야함(대신에 batch fetch size? 옵션 주면 N -> 1 쿼리로 변경 가능)
 * <p>
 * V4.JPA에서 DTO로 바로 조회, 컬렉션 N 조회 (1+NQuery)
 * - 페이징 가능
 * <p>
 * V5.JPA에서 DTO로 바로 조회, 컬렉션 1 조회 최적화 버전 (1+1Query)
 * - 페이징 가능
 * <p>
 * V6. JPA에서 DTO로 바로 조회, 플랫 데이터(1Query) (1 Query)
 * - 페이징 불가능...
 */
@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderQueryService orderQueryService;
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    /**
     * V1. 엔티티 직접 노출
     * - Hibernate5Module 모듈 등록, LAZY=null 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAllByCriteria(OrderSearch.builder().build());
        for (Order order : orders) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.forEach(o -> o.getItem().getName()); //Lazy 강제 초기화
        }
        return orders;
    }

    /**
     * OSIV ON 인 경우 - 트랜잭션 밖에서 지연 로딩 - org.hibernate.LazyInitializationException: could not initialize proxy
     */
    @GetMapping("/api/v2.1/orders")
    public List<OrderResponseDto> ordersV2_1() {
        List<Order> orders = orderRepository.findAllByCriteria(OrderSearch.builder().build());
        return orders.stream()
                .map(OrderResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * OSIV OFF 인 경우 - 트랜잭션 안에서 지연 로딩
     */
    @GetMapping("/api/v2.2/orders")
    public List<OrderDto> ordersV2_2() {
        return orderQueryService.findAll(OrderSearch.builder().build());
    }

    @GetMapping("/api/v3/orders")
    public List<OrderResponseDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberAndDeliveryAndItem();
        return orders.stream()
                .map(OrderResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * V3.1 엔티티를 조회해서 DTO로 변환 페이징 고려
     * - ToOne 관계만 우선 모두 페치 조인으로 최적화
     * - 컬렉션 관계는 hibernate.default_batch_fetch_size, @BatchSize로 최적화
     */
    @GetMapping("/api/v3.1/orders")
    public List<OrderResponseDto> ordersV3Page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                               @RequestParam(value = "limit", defaultValue = "100") int limit) {
        /**
         * DTO 로 변환하면서 Order -> OrderItem -> Item 으로 레이지 로딩이 일어난다.
         * 지연 로딩 성능 최적화를 위해 hibernate.default_batch_fetch_size , @BatchSize 를 적용한다.
         * 개별로 설정하려면 @BatchSize 를 적용하면 된다. (컬렉션은 컬렉션 필드에, 엔티티는 엔티티 클래스에 적용)
         * Order.orderItems 컬렉션 필드에 적용
         * Item 클래스에 적용
         */
        List<Order> orders = orderRepository.findPagingWithMemberAndDelivery(offset, limit);
        return orders.stream()
                .map(OrderResponseDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findAllByDto_optimization();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();
        return flats.stream()
                .collect(Collectors.groupingBy(o -> OrderQueryDto.builder()
                                                       .orderId(o.getOrderId())
                                                       .name(o.getName())
                                                       .orderDate(o.getOrderDate())
                                                       .orderStatus(o.getOrderStatus())
                                                       .address(o.getAddress())
                                                       .build(),
                                               Collectors.mapping(o -> OrderItemQueryDto.builder()
                                                                          .orderId(o.getOrderId())
                                                                          .itemName(o.getItemName())
                                                                          .orderPrice(o.getOrderPrice())
                                                                          .count(o.getCount())
                                                                          .build(),
                                                                  Collectors.toList())
                ))
                .entrySet()
                .stream()
                .map(e -> OrderQueryDto.builder()
                        .orderId(e.getKey().getOrderId())
                        .name(e.getKey().getName())
                        .orderDate(e.getKey().getOrderDate())
                        .orderStatus(e.getKey().getOrderStatus())
                        .address(e.getKey().getAddress())
                        .orderItems(e.getValue())
                        .build())
                .collect(Collectors.toList());
    }
}
