package com.david.auth_layer_architecture.domain.events;

import com.david.auth_layer_architecture.domain.entity.Credential;
import lombok.Getter;

@Getter
public class UserRegisteredEvent extends DomainEvent {

    private final Credential credential;

    public UserRegisteredEvent(Credential credential) {
        super();
        this.credential = credential;
    }
}
