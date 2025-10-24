package com.github.sidneymiranda.order.command.domain.event;

import com.github.sidneymiranda.order.command.domain.OrderStatus;
import java.time.Instant;
import java.util.UUID;

public class OrderCancelledEvent extends DomainEvent {
    private final UUID orderId;
    private final OrderStatus currentStatus;
    private final Instant cancelledAt;

    private OrderCancelledEvent(Builder builder) {
        super();
        this.orderId = builder.orderId;
        this.currentStatus = builder.currentStatus;
        this.cancelledAt = builder.cancelledAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID orderId;
        private OrderStatus currentStatus;
        private Instant cancelledAt;

        public Builder orderId(UUID orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder status(OrderStatus status) {
            this.currentStatus = status;
            return this;
        }

        public Builder cancelledAt(Instant cancelledAt) {
            this.cancelledAt = cancelledAt;
            return this;
        }

        public OrderCancelledEvent build() {
            if (currentStatus != OrderStatus.CANCELLED) {
                throw new IllegalStateException("Order status must be CANCELLED to build OrderCancelledEvent");
            }
            if (cancelledAt == null) {
                this.cancelledAt = Instant.now();
            }
            if (orderId == null) {
                throw new IllegalStateException("OrderId cannot be null");
            }
            return new OrderCancelledEvent(this);
        }
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
