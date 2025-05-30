package com.david.auth_layer_architecture.infrestructure.events;

import com.david.auth_layer_architecture.infrestructure.services.interfaces.IEmailService;
import com.david.auth_layer_architecture.infrestructure.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.domain.events.SendEmailEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SendEmailEventHandler {
    private final IEmailService emailService;

    @EventListener
    public void handle(SendEmailEvent event) {
        switch (event.getTypeEmail()) {
            case CommonConstants.TYPE_ACCESS_TOKEN_TO_VERIFY_ACCOUNT:
                this.emailService.sendEmailVerifyAccount(event.getCredential().getEmail(), event.getAccessToken(), event.getRefreshToken());
                break;
            case CommonConstants.TYPE_ACCESS_TOKEN_TO_CHANGE_PASSWORD:
                this.emailService.sendEmailRecoveryAccount(event.getCredential().getEmail(), event.getAccessToken());
            default:
                break;
        }
    }

}
