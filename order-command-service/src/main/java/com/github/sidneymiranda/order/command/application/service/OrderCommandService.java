package com.github.sidneymiranda.order.command.application.service;

import com.eventstore.dbclient.EventData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sidneymiranda.order.command.application.port.in.CancelOrderCommand;
import com.github.sidneymiranda.order.command.application.port.in.ConfirmOrderCommand;
import com.github.sidneymiranda.order.command.application.port.in.CreateOrderCommand;
import com.github.sidneymiranda.order.command.application.port.in.CreateOrderUseCase;
import com.github.sidneymiranda.order.command.application.port.out.EventPublisher;
import com.github.sidneymiranda.order.command.application.port.out.EventStoreRepository;
import com.github.sidneymiranda.order.command.domain.Order;
import com.github.sidneymiranda.order.command.domain.event.DomainEvent;
import com.github.sidneymiranda.order.command.domain.event.OrderCancelledEvent;
import com.github.sidneymiranda.order.command.domain.event.OrderConfirmedEvent;
import com.github.sidneymiranda.order.command.domain.event.OrderCreatedEvent;
import com.github.sidneymiranda.order.command.domain.exception.OrderDomainException;
import com.github.sidneymiranda.order.command.domain.valueobject.Money;
import com.github.sidneymiranda.order.command.domain.valueobject.Quantity;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderCommandService implements CreateOrderUseCase {
    private final EventStoreRepository eventStoreRepository;
    private final EventPublisher eventPublisher;
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<UUID, Order> orderStore = new ConcurrentHashMap<>();

    public OrderCommandService(EventStoreRepository eventStoreRepository,
                               EventPublisher eventPublisher,
                               ObjectMapper objectMapper) {
        this.eventStoreRepository = eventStoreRepository;
        this.eventPublisher = eventPublisher;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(CreateOrderCommand command) {
        Order order = Order.builder()
                .orderId(command.orderId())
                .client(command.client())
                .productId(command.productId())
                .unitPrice(new Money(command.unitPrice()))
                .quantity(new Quantity(command.quantity()))
                .createdAt(Instant.now())
                .build();

        orderStore.put(order.getOrderId(), order);

        DomainEvent event = OrderCreatedEvent.builder()
                .orderId(order.getOrderId())
                .client(order.getClient())
                .productId(order.getProductId())
                .unitPrice(order.getUnitPrice())
                .quantity(order.getQuantity())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();

        saveAndPublishEvent(EventType.CREATED, event, order.getOrderId());
    }

    @Override
    public void confirmOrder(ConfirmOrderCommand command) {
        Order order = orderStore.get(command.orderId());
        if (order == null) {
            throw new OrderDomainException("Order not found with id: " + command.orderId());
        }

        order.confirm();
        DomainEvent event = OrderConfirmedEvent.builder()
                .orderId(order.getOrderId())
                .status(order.getStatus())
                .confirmedAt(Instant.now())
                .build();

        saveAndPublishEvent(EventType.CONFIRMED, event, order.getOrderId());
    }

    @Override
    public void cancelOrder(CancelOrderCommand command) {
        Order order = orderStore.get(command.orderId());
        if (order == null) {
            throw new OrderDomainException("Order not found with id: " + command.orderId());
        }

        order.cancel();
        DomainEvent event = OrderCancelledEvent.builder()
                .orderId(order.getOrderId())
                .status(order.getStatus())
                .cancelledAt(Instant.now())
                .build();

        saveAndPublishEvent(EventType.CANCELED, event, order.getOrderId());
    }

    private void saveAndPublishEvent(EventType eventType, DomainEvent event, UUID orderId) {
        String eventJson = writeValueAsString(event);
        EventData eventData = EventData.builderAsJson(eventType.toString(), eventJson.getBytes())
                .eventId(event.getEventId())
                .build();

        eventStoreRepository.appendStream("order-" + orderId, List.of(eventData));
        eventPublisher.publish(event);
    }

    private String writeValueAsString(Object event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize event: " + e.getMessage(), e);
        }
    }
}
