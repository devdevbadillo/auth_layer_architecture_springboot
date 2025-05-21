package com.david.auth_layer_architecture.domain.events;


public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
