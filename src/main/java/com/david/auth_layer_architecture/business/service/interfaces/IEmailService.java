package com.david.auth_layer_architecture.business.service.interfaces;

import jakarta.mail.MessagingException;

public interface IEmailService {
    void sendEmailRecoveryAccount(String email, String token) throws MessagingException;

    void sendEmailVerifyAccount(String email, String accessToken, String refreshToken) throws MessagingException;
}
