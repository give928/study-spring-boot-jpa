package com.give928.springboot.jpa.domain.delivery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.give928.springboot.jpa.domain.common.Address;
import com.give928.springboot.jpa.domain.order.Order;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //ENUM [READY(준비), COMP(배송)]

    public void setOrder(Order order) {
        this.order = order;
    }

    public boolean isComp() {
        return getStatus() != null && getStatus().isComp();
    }
}
