package com.github.sidneymiranda.order.command.domain;

public enum OrderStatus {
    CREATED("Created"),
    CONFIRMED("Confirmed"),
    CANCELLED("Cancelled");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
