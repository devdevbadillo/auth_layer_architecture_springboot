package com.david.auth_layer_architecture.business.service.interfaces;

import com.david.auth_layer_architecture.domain.dto.response.SignInResponse;
import com.david.auth_layer_architecture.domain.entity.AccessToken;
import com.david.auth_layer_architecture.domain.entity.Credential;

public interface ITokenService {
    SignInResponse generateAuthTokens(Credential credential);

    SignInResponse generateVerifyAccountTokens(Credential credential);

    String saveAccessToken(Credential credential, String type, Integer expiration);

    String refreshAccessTokenToAccessApp(String refreshToken);

    void revokePairTokens(String accessTokenId);

    void revokeAccessToken(String accessTokenId);

    AccessToken getAccessToken(String accessTokenId);
}
