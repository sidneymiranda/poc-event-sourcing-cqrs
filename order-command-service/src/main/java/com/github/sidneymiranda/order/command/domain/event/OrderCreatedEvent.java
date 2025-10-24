package com.github.sidneymiranda.order.command.domain.event;

import com.github.sidneymiranda.order.command.domain.OrderStatus;
import com.github.sidneymiranda.order.command.domain.valueobject.Money;
import com.github.sidneymiranda.order.command.domain.valueobject.Quantity;

import java.time.Instant;
import java.util.UUID;

public class OrderCreatedEvent extends DomainEvent {
    private final UUID orderId;
    private final String client;
    private final String productId;
    private final Money unitPrice;
    private final Quantity quantity;
    private final Money totalAmount;
    private final OrderStatus status;
    private final Instant createdAt;

    private OrderCreatedEvent(Builder builder) {
        super();
        this.orderId = builder.orderId;
        this.client = builder.client;
        this.productId = builder.productId;
        this.unitPrice = builder.unitPrice;
        this.quantity = builder.quantity;
        this.totalAmount = builder.totalAmount;
        this.status = builder.status;
        this.createdAt = builder.createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID orderId;
        private String client;
        private String productId;
        private Money unitPrice;
        private Quantity quantity;
        private Money totalAmount;
        private OrderStatus status;
        private Instant createdAt;

        public Builder orderId(UUID orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder client(String client) {
            this.client = client;
            return this;
        }

        public Builder productId(String productId) {
            this.productId = productId;
            return this;
        }

        public Builder unitPrice(Money unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public Builder quantity(Quantity quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder totalAmount(Money totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public OrderCreatedEvent build() {
            if (orderId == null) {
                throw new IllegalStateException("OrderId cannot be null");
            }
            if (client == null || client.trim().isEmpty()) {
                throw new IllegalStateException("Client cannot be null or empty");
            }
            if (productId == null || productId.trim().isEmpty()) {
                throw new IllegalStateException("ProductId cannot be null or empty");
            }
            if (unitPrice == null) {
                throw new IllegalStateException("UnitPrice cannot be null");
            }
            if (quantity == null) {
                throw new IllegalStateException("Quantity cannot be null");
            }
            if (totalAmount == null) {
                throw new IllegalStateException("TotalAmount cannot be null");
            }
            if (status != OrderStatus.CREATED) {
                throw new IllegalStateException("Order status must be CREATED to build OrderCreatedEvent");
            }
            if (createdAt == null) {
                this.createdAt = Instant.now();
            }
            return new OrderCreatedEvent(this);
        }
    }

    public UUID getOrderId() {
        return orderId;
    }

    public String getClient() {
        return client;
    }

    public String getProductId() {
        return productId;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
