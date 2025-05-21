package com.david.auth_layer_architecture.business.events;

import com.david.auth_layer_architecture.business.service.interfaces.IAccessTokenService;
import com.david.auth_layer_architecture.business.service.interfaces.IRefreshTokenService;
import com.david.auth_layer_architecture.business.service.interfaces.ITokenService;
import com.david.auth_layer_architecture.common.utils.JwtUtil;
import com.david.auth_layer_architecture.common.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.domain.dto.response.SignInResponse;
import com.david.auth_layer_architecture.domain.entity.AccessToken;
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
        SignInResponse response = this.tokenService.generateVerifyAccountTokens(credential);

        domainEventPublisher.publish(new SendEmailEvent(credential, response.accessToken(), CommonConstants.TYPE_ACCESS_TOKEN_TO_VERIFY_ACCOUNT , response.refreshToken()));
    }
}
