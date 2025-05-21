package com.david.auth_layer_architecture.domain.events;

import com.david.auth_layer_architecture.domain.entity.Credential;
import lombok.Getter;

@Getter
public class SendEmailEvent extends DomainEvent{
    private final Credential credential;
    private final String accessToken;
    private final String typeEmail;
    private String refreshToken;

    public SendEmailEvent(Credential credential, String accessToken, String typeEmail, String refreshToken) {
        super();
        this.credential = credential;
        this.accessToken = accessToken;
        this.typeEmail = typeEmail;
        this.refreshToken = refreshToken;
    }

    public SendEmailEvent(Credential credential, String accessToken, String typeEmail) {
        super();
        this.credential = credential;
        this.typeEmail = typeEmail;
        this.accessToken = accessToken;
    }
}
