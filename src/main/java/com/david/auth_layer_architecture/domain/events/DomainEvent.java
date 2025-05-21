package com.david.auth_layer_architecture.domain.events;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

public class DomainEvent {

    private final LocalDateTime occurredOn;

    public DomainEvent() {
        this.occurredOn = LocalDateTime.now();
    }

    public LocalDateTime occurredOn() {
        return this.occurredOn;
    }

}
