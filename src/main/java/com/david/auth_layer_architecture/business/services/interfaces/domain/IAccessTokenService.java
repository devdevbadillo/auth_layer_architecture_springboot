package com.david.auth_layer_architecture.business.services.interfaces.domain;

import com.david.auth_layer_architecture.domain.entity.AccessToken;
import com.david.auth_layer_architecture.domain.entity.Credential;

public interface IAccessTokenService {

    AccessToken saveAccessToken(String accessToken, Credential credential, String typeToken);

    AccessToken saveAccessTokenToAccessApp(String accessToken, Credential credential);

    void saveAccessTokenToAccessAppWithRefreshToken(AccessToken oldAccessToken, String accessToken);

    AccessToken getTokenByAccessTokenId(String accessTokenId);

    void deleteAccessToken(String accessTokenId);
}
