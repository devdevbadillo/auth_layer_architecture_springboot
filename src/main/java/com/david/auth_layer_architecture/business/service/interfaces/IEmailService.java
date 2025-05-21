package com.david.auth_layer_architecture.business.service.interfaces;


public interface IEmailService {
    void sendEmailRecoveryAccount(String email, String token);

    void sendEmailVerifyAccount(String email, String accessToken, String refreshToken);
}
