package com.david.auth_layer_architecture.infrestructure.events;

import com.david.auth_layer_architecture.business.interfaces.domain.ITokenService;
import com.david.auth_layer_architecture.common.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.presentation.dto.response.PairTokenResponse;
import com.david.auth_layer_architecture.domain.entity.Credential;
import com.david.auth_layer_architecture.domain.events.DomainEventPublisher;
import com.david.auth_layer_architecture.domain.events.SendEmailEvent;
import com.david.auth_layer_architecture.domain.events.UserRegisteredEvent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.event.EventListener;

@Component
@Transactional
@AllArgsConstructor
public class UserRegisteredEventHandler {

    private final ITokenService tokenService;
    private final DomainEventPublisher domainEventPublisher;

    @EventListener
    public void handle(UserRegisteredEvent event) {
        Credential credential = event.getCredential();
        PairTokenResponse response = this.tokenService.generateVerifyAccountTokens(credential);

        domainEventPublisher.publish(new SendEmailEvent(credential, response.accessToken(), CommonConstants.TYPE_ACCESS_TOKEN_TO_VERIFY_ACCOUNT , response.refreshToken()));
    }
}
