package com.david.auth_layer_architecture.business.service.interfaces;

import com.david.auth_layer_architecture.domain.entity.AccessToken;
import com.david.auth_layer_architecture.domain.entity.Credential;
import com.david.auth_layer_architecture.domain.entity.RefreshToken;

public interface IRefreshTokenService {

    void saveRefreshTokenToAccessApp(String refreshToken, Credential credential, AccessToken accessToken);

    RefreshToken findRefreshTokenByRefreshTokenId(String refreshTokenId);

    RefreshToken findRefreshTokenByAccessToken(Long accessTokenId);

    void deleteRefreshToken(AccessToken accessToken);

}
