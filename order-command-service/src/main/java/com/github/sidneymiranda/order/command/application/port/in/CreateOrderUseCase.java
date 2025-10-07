package com.github.sidneymiranda.order.command.application.port.in;

public interface CreateOrderUseCase {
    void handle(CreateOrderCommand command);
    void confirmOrder(ConfirmOrderCommand command);
    void cancelOrder(CancelOrderCommand command);
}
