package com.give928.springboot.jpa.service;

import com.give928.springboot.jpa.domain.common.Address;
import com.give928.springboot.jpa.domain.member.Member;
import com.give928.springboot.jpa.domain.order.Order;
import com.give928.springboot.jpa.domain.order.OrderStatus;
import com.give928.springboot.jpa.domain.item.Book;
import com.give928.springboot.jpa.exception.NotEnoughStockException;
import com.give928.springboot.jpa.repository.order.OrderRepository;
import com.give928.springboot.jpa.service.order.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    EntityManager em;

    @Test
    void 상품주문() {
        // given
        Member member = createMember();
        final int itemPrice = 10000;
        Book book = createItem("JPA 기본", itemPrice, 10);
        final int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order savedOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, savedOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, savedOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량 이다.", itemPrice * orderCount, savedOrder.getTotalPrice());
        assertEquals("주문 수량만킄 재고가 줄어야 한다.", 8, book.getStockQuantity());
    }

    @Test
    void 상품주문_재고수량초과() {
        // given
        Member member = createMember();
        Book book = createItem("JPA 기본", 10000, 10);
        final int orderCount = 11;

        // when
        Executable executable = () -> orderService.order(member.getId(), book.getId(), orderCount);

        // then
        assertThrows(NotEnoughStockException.class, executable);
    }

    @Test
    void 주문취소() {
        // given
        Member member = createMember();
        final int stockQuantity = 10;
        Book book = createItem("JPA 기본", 10000, stockQuantity);
        final int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order savedOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소시 상태는 CANCEL 이다.", OrderStatus.CANCEL, savedOrder.getStatus());
        assertEquals("주문이 취소된 상품은 주문수량만큼 재고가 증가해야 한다.", stockQuantity, book.getStockQuantity());
    }

    private Member createMember() {
        Member member = Member.builder()
                .name("회원1")
                .address(Address.builder().city("서울").street("문래로").zipcode("12345").build())
                .build();
        em.persist(member);
        return member;
    }

    private Book createItem(String name, int price, int stockQuantity) {
        Book book = Book.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .build();
        em.persist(book);
        return book;
    }
}
