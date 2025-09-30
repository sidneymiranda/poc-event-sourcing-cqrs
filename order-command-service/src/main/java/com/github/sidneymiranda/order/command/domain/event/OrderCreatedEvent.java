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

    public OrderCreatedEvent(UUID orderId, String client, String productId,
                           Money unitPrice, Quantity quantity, Money totalAmount,
                           OrderStatus status, Instant createdAt) {
        super();
        this.orderId = orderId;
        this.client = client;
        this.productId = productId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getOrderId() { return orderId; }
    public String getClient() { return client; }
    public String getProductId() { return productId; }
    public Money getUnitPrice() { return unitPrice; }
    public Quantity getQuantity() { return quantity; }
    public Money getTotalAmount() { return totalAmount; }
    public OrderStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
