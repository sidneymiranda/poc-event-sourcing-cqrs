package com.github.sidneymiranda.order.command.application.port.out;

import com.github.sidneymiranda.order.command.domain.event.DomainEvent;

public interface EventPublisher {
    void publish(DomainEvent event);
}
