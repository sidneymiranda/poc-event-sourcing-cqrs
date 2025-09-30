package com.github.sidneymiranda.order.command.domain;

import com.github.sidneymiranda.order.command.domain.exception.OrderDomainException;
import com.github.sidneymiranda.order.command.domain.valueobject.Money;
import com.github.sidneymiranda.order.command.domain.valueobject.Quantity;

import java.time.Instant;
import java.util.UUID;

public class Order {
    private final UUID orderId;
    private final String client;
    private final String productId;
    private final Money unitPrice;
    private final Quantity quantity;
    private final Money totalAmount;
    private final Instant createdAt;
    private OrderStatus status;

    private Order(Builder builder) {
        this.orderId = builder.orderId;
        this.client = builder.client;
        this.productId = builder.productId;
        this.unitPrice = builder.unitPrice;
        this.quantity = builder.quantity;
        this.totalAmount = builder.unitPrice.multiply(builder.quantity.value());
        this.createdAt = builder.createdAt;
        this.status = OrderStatus.CREATED;
        validateOrder();
    }

    private void validateOrder() {
        if (orderId == null) {
            throw new OrderDomainException("Order id cannot be null");
        }
        if (client == null || client.trim().isEmpty()) {
            throw new OrderDomainException("Client cannot be null or empty");
        }
        if (productId == null || productId.trim().isEmpty()) {
            throw new OrderDomainException("Product id cannot be null or empty");
        }
        if (unitPrice == null) {
            throw new OrderDomainException("Unit price cannot be null");
        }
        if (quantity == null) {
            throw new OrderDomainException("Quantity cannot be null");
        }
        if (createdAt == null) {
            throw new OrderDomainException("Created date cannot be null");
        }
    }

    public void confirm() {
        if (this.status != OrderStatus.CREATED) {
            throw new OrderDomainException("Order can only be confirmed when in CREATED status. Current status: " + status);
        }
        this.status = OrderStatus.CONFIRMED;
    }

    public void cancel() {
        if (this.status == OrderStatus.CANCELLED) {
            throw new OrderDomainException("Order is already cancelled");
        }
        if (this.status == OrderStatus.CONFIRMED) {
            throw new OrderDomainException("Cannot cancel a confirmed order");
        }
        this.status = OrderStatus.CANCELLED;
    }

    public UUID getOrderId() { return orderId; }
    public String getClient() { return client; }
    public String getProductId() { return productId; }
    public Money getUnitPrice() { return unitPrice; }
    public Quantity getQuantity() { return quantity; }
    public Money getTotalAmount() { return totalAmount; }
    public OrderStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID orderId;
        private String client;
        private String productId;
        private Money unitPrice;
        private Quantity quantity;
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

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
