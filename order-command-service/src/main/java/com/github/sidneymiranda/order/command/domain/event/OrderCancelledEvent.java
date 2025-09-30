package com.github.sidneymiranda.order.command.domain.event;

import com.github.sidneymiranda.order.command.domain.OrderStatus;
import java.time.Instant;
import java.util.UUID;

public class OrderCancelledEvent extends DomainEvent {
    private final UUID orderId;
    private final OrderStatus currentStatus;
    private final Instant cancelledAt;

    public OrderCancelledEvent(UUID orderId, OrderStatus status, Instant cancelledAt) {
        super();
        this.orderId = orderId;
        this.currentStatus = status;
        this.cancelledAt = cancelledAt;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public OrderStatus getCurrentStatus() {
        return currentStatus;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }
}
