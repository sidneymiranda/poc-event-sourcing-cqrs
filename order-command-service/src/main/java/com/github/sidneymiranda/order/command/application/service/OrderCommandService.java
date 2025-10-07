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

        DomainEvent event = new OrderCreatedEvent(
                order.getOrderId(),
                order.getClient(),
                order.getProductId(),
                order.getUnitPrice(),
                order.getQuantity(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt()
        );

        saveAndPublishEvent(EventType.CREATED, event, order.getOrderId());
    }

    @Override
    public void confirmOrder(ConfirmOrderCommand command) {
        Order order = orderStore.get(command.orderId());
        if (order == null) {
            throw new OrderDomainException("Order not found with id: " + command.orderId());
        }

        order.confirm();
        DomainEvent event = new OrderConfirmedEvent(order.getOrderId(), order.getStatus(), Instant.now());

        saveAndPublishEvent(EventType.CONFIRMED, event, order.getOrderId());
    }

    @Override
    public void cancelOrder(CancelOrderCommand command) {
        Order order = orderStore.get(command.orderId());
        if (order == null) {
            throw new OrderDomainException("Order not found with id: " + command.orderId());
        }

        order.cancel();
        DomainEvent event = new OrderCancelledEvent(order.getOrderId(), order.getStatus(), Instant.now());

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
