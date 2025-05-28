package com.david.auth_layer_architecture.infrestructure.services.interfaces;


public interface IEmailService {
    void sendEmailRecoveryAccount(String email, String token);

    void sendEmailVerifyAccount(String email, String accessToken, String refreshToken);
}
