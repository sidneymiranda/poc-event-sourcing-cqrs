package com.github.sidneymiranda.order.command.domain.event;

import com.github.sidneymiranda.order.command.domain.OrderStatus;

import java.time.Instant;
import java.util.UUID;

public class OrderConfirmedEvent extends DomainEvent {
    private final UUID orderId;
    private final OrderStatus currentStatus;
    private final Instant confirmedAt;

    private OrderConfirmedEvent(Builder builder) {
        super();
        this.orderId = builder.orderId;
        this.currentStatus = builder.currentStatus;
        this.confirmedAt = builder.confirmedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID orderId;
        private OrderStatus currentStatus;
        private Instant confirmedAt;

        public Builder orderId(UUID orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder status(OrderStatus status) {
            this.currentStatus = status;
            return this;
        }

        public Builder confirmedAt(Instant confirmedAt) {
            this.confirmedAt = confirmedAt;
            return this;
        }

        public OrderConfirmedEvent build() {
            if (currentStatus != OrderStatus.CONFIRMED) {
                throw new IllegalStateException("Order status must be CONFIRMED to build OrderConfirmedEvent");
            }
            if (confirmedAt == null) {
                this.confirmedAt = Instant.now();
            }
            if (orderId == null) {
                throw new IllegalStateException("OrderId cannot be null");
            }
            return new OrderConfirmedEvent(this);
        }
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
