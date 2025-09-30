package com.github.sidneymiranda.order.command.domain.event;

import com.github.sidneymiranda.order.command.domain.OrderStatus;

import java.time.Instant;
import java.util.UUID;

public class OrderConfirmedEvent extends DomainEvent {
    private final UUID orderId;
    private final OrderStatus currentStatus;
    private final Instant confirmedAt;

    public OrderConfirmedEvent(UUID orderId, OrderStatus status, Instant confirmedAt) {
        super();
        this.orderId = orderId;
        this.currentStatus = status;
        this.confirmedAt = confirmedAt;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public OrderStatus getCurrentStatus() {
        return currentStatus;
    }

    public Instant getConfirmedAt() {
        return confirmedAt;
    }
}
