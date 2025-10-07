package com.github.sidneymiranda.order.command.application.port.in;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateOrderCommand(
    UUID orderId,
    String client,
    String productId,
    BigDecimal unitPrice,
    int quantity
) {
    public CreateOrderCommand {
        if (orderId == null) {
            throw new IllegalArgumentException("Order id cannot be null");
        }
        if (client == null || client.trim().isEmpty()) {
            throw new IllegalArgumentException("Client cannot be null or empty");
        }
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product id cannot be null or empty");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Unit price must be greater than zero");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }
}
