package com.github.sidneymiranda.order.command.application.service;

public enum EventType {
    CREATED("OrderCreatedEvent"),
    CANCELED("OrderCancelledEvent"),
    CONFIRMED("OrderConfirmedEvent");

    private final String value;

    EventType(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return value;
    }
}
