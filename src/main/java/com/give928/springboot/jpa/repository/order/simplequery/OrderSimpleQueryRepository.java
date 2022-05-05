package com.give928.springboot.jpa.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {
    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderQueryDtos() {
        String qlString = "select new com.give928.springboot.jpa.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                " from Order as o" +
                " inner join o.member as m" +
                " inner join o.delivery as d";
        return em.createQuery(qlString, OrderSimpleQueryDto.class)
                .getResultList();
    }
}
