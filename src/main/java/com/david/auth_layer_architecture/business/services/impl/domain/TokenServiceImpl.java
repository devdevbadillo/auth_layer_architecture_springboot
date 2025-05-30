package com.david.auth_layer_architecture.business.services.impl.domain;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.david.auth_layer_architecture.business.services.interfaces.domain.IAccessTokenService;
import com.david.auth_layer_architecture.business.services.interfaces.domain.IRefreshTokenService;
import com.david.auth_layer_architecture.business.services.interfaces.domain.ITokenService;
import com.david.auth_layer_architecture.infrestructure.utils.JwtUtil;
import com.david.auth_layer_architecture.infrestructure.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.presentation.dto.response.PairTokenResponse;
import com.david.auth_layer_architecture.domain.entity.AccessToken;
import com.david.auth_layer_architecture.domain.entity.Credential;
import com.david.auth_layer_architecture.domain.entity.RefreshToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenServiceImpl implements ITokenService {
    private final JwtUtil jwtUtil;
    private final IAccessTokenService accessTokenService;
    private final IRefreshTokenService refreshTokenService;


    @Override
    public PairTokenResponse generateAuthTokens(Credential credential) {
        String tokenToAccessApp = jwtUtil.generateAccessToken(
                credential,
                CommonConstants.EXPIRATION_TOKEN_TO_ACCESS_APP,
                CommonConstants.TYPE_ACCESS_TOKEN_TO_ACCESS_APP
        );

        String refreshTokenToAccessApp = jwtUtil.generateRefreshToken(
                credential,
                CommonConstants.EXPIRATION_REFRESH_TOKEN_TO_ACCESS_APP,
                CommonConstants.TYPE_REFRESH_TOKEN_TO_ACCESS_APP
        );

        AccessToken accessToken = accessTokenService.saveAccessTokenToAccessApp(tokenToAccessApp, credential);

        refreshTokenService.saveRefreshToken(refreshTokenToAccessApp, credential, accessToken, CommonConstants.TYPE_REFRESH_TOKEN_TO_ACCESS_APP);

        return new PairTokenResponse(tokenToAccessApp, refreshTokenToAccessApp);
    }

    @Override
    public PairTokenResponse generateVerifyAccountTokens(Credential credential) {
        String accessTokenToVerifyAccount = jwtUtil.generateAccessToken(
                credential,
                CommonConstants.EXPIRATION_TOKEN_TO_VERIFY_ACCOUNT,
                CommonConstants.TYPE_ACCESS_TOKEN_TO_VERIFY_ACCOUNT);

        String refreshTokenToVerifyAccount = jwtUtil.generateRefreshToken(
                credential,
                CommonConstants.EXPIRATION_REFRESH_TOKEN_TO_VERIFY_ACCOUNT,
                CommonConstants.TYPE_REFRESH_TOKEN_TO_VERIFY_ACCOUNT);

        AccessToken accessToken = this.accessTokenService.saveAccessToken(
                accessTokenToVerifyAccount,
                credential,
                CommonConstants.TYPE_ACCESS_TOKEN_TO_VERIFY_ACCOUNT);

        this.refreshTokenService.saveRefreshToken(refreshTokenToVerifyAccount, credential, accessToken, CommonConstants.TYPE_REFRESH_TOKEN_TO_VERIFY_ACCOUNT);

        return new PairTokenResponse(accessTokenToVerifyAccount, refreshTokenToVerifyAccount);

    }

    @Override
    public String saveAccessToken(Credential credential, String type, Integer expiration) {
        String token = jwtUtil.generateAccessToken(credential, expiration, type);

        this.accessTokenService.saveAccessToken(token, credential, type);

        return token;
    }

    @Override
    public String refreshAccessTokenToAccessApp(String refreshToken) {
        DecodedJWT decodedJWT = jwtUtil.validateToken(refreshToken);
        jwtUtil.validateTypeToken(decodedJWT, CommonConstants.TYPE_REFRESH_TOKEN_TO_ACCESS_APP);

        RefreshToken refreshTokenEntity = this.refreshTokenService.findRefreshTokenByRefreshTokenId(decodedJWT.getClaim("jti").asString());
        String accessToken = jwtUtil.generateAccessToken(refreshTokenEntity.getCredential(), CommonConstants.EXPIRATION_TOKEN_TO_ACCESS_APP, CommonConstants.TYPE_ACCESS_TOKEN_TO_ACCESS_APP);

        this.accessTokenService.saveAccessTokenToAccessAppWithRefreshToken(refreshTokenEntity.getAccessToken(), accessToken);
        return accessToken;
    }

    @Override
    public void revokePairTokens(String accessTokenId) {
        AccessToken accessToken = this.getAccessToken(accessTokenId);
        this.refreshTokenService.deleteRefreshToken(accessToken);
    }

    @Override
    public void revokeAccessToken(String accessTokenId) {
        accessTokenService.deleteAccessToken(accessTokenId);
    }

    @Override
    public AccessToken getAccessToken(String accessTokenId) {
        return this.accessTokenService.getTokenByAccessTokenId(accessTokenId);
    }
}
