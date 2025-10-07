package com.github.sidneymiranda.order.command.application.port.in;

import java.util.UUID;

public record CancelOrderCommand(UUID orderId) {
    public CancelOrderCommand {
        if (orderId == null) {
            throw new IllegalArgumentException("Order id cannot be null");
        }
    }
}
