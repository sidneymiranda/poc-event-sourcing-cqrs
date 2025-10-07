package com.github.sidneymiranda.order.command.application.port.in;

import java.util.UUID;

public record ConfirmOrderCommand(UUID orderId) {
    public ConfirmOrderCommand {
        if (orderId == null) {
            throw new IllegalArgumentException("Order id cannot be null");
        }
    }
}
