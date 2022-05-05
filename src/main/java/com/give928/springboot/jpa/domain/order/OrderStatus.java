package com.give928.springboot.jpa.domain.order;

public enum OrderStatus {
    ORDER, CANCEL;

    public boolean isOrder() {
        return this == ORDER;
    }
}
