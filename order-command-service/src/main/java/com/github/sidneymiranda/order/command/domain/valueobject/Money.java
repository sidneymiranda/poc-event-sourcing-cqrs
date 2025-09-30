package com.github.sidneymiranda.order.command.domain.valueobject;

import com.github.sidneymiranda.order.command.domain.exception.OrderDomainException;
import java.math.BigDecimal;

public record Money(BigDecimal amount) {
    public Money {
        if (amount == null) {
            throw new OrderDomainException("Amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new OrderDomainException("Amount cannot be negative");
        }
    }

    public Money add(Money money) {
        return new Money(this.amount.add(money.amount));
    }

    public Money multiply(int quantity) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(quantity)));
    }
}
