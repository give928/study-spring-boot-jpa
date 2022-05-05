package com.give928.springboot.jpa.domain.delivery;

public enum DeliveryStatus {
    READY, COMP;

    public boolean isComp() {
        return this == DeliveryStatus.COMP;
    }
}
