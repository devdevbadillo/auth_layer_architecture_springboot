package com.david.auth_layer_architecture.business.services.interfaces.domain;

import com.david.auth_layer_architecture.presentation.dto.response.PairTokenResponse;
import com.david.auth_layer_architecture.domain.entity.AccessToken;
import com.david.auth_layer_architecture.domain.entity.Credential;

public interface ITokenService {
    PairTokenResponse generateAuthTokens(Credential credential);

    PairTokenResponse generateVerifyAccountTokens(Credential credential);

    String saveAccessToken(Credential credential, String type, Integer expiration);

    String refreshAccessTokenToAccessApp(String refreshToken);

    void revokePairTokens(String accessTokenId);

    void revokeAccessToken(String accessTokenId);

    AccessToken getAccessToken(String accessTokenId);
}
