package com.give928.springboot.jpa.service.order.query;

import com.give928.springboot.jpa.domain.order.Order;
import com.give928.springboot.jpa.repository.order.OrderRepository;
import com.give928.springboot.jpa.repository.order.OrderSearch;
import com.give928.springboot.jpa.service.order.query.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryService {
    private final OrderRepository orderRepository;

    public List<OrderDto> findAll(OrderSearch orderSearch) {
        List<Order> orders = orderRepository.findAllByCriteria(orderSearch);
        return orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }
}
