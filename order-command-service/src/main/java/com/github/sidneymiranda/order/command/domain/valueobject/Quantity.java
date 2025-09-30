package com.github.sidneymiranda.order.command.domain.valueobject;

import com.github.sidneymiranda.order.command.domain.exception.OrderDomainException;

public record Quantity(int value) {
    public Quantity {
        if (value <= 0) {
            throw new OrderDomainException("Quantity must be greater than zero");
        }
    }
}
