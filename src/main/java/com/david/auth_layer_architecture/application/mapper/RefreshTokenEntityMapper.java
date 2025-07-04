package com.david.auth_layer_architecture.application.mapper;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.david.auth_layer_architecture.business.services.interfaces.domain.ITypeTokenService;
import com.david.auth_layer_architecture.infrestructure.utils.JwtUtil;
import com.david.auth_layer_architecture.domain.entity.AccessToken;
import com.david.auth_layer_architecture.domain.entity.Credential;
import com.david.auth_layer_architecture.domain.entity.RefreshToken;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenEntityMapper {
    private final JwtUtil jwtUtil;
    private final ITypeTokenService typeTokenService;

    public RefreshTokenEntityMapper(JwtUtil jwtUtil, ITypeTokenService typeTokenService) {
        this.jwtUtil = jwtUtil;
        this.typeTokenService = typeTokenService;
    }

    public RefreshToken toRefreshTokenEntity(String refreshToken, Credential credential, String typeToken, AccessToken accessTokenEntity) {
        DecodedJWT tokenDecoded = this.jwtUtil.validateToken(refreshToken);
        String tokenId = this.jwtUtil.getSpecificClaim(tokenDecoded, "jti").asString();

        return RefreshToken.builder()
                .accessToken(accessTokenEntity)
                .refreshTokenId(tokenId)
                .credential(credential)
                .typeToken(this.typeTokenService.getTypeToken(typeToken))
                .creationDate(tokenDecoded.getIssuedAt())
                .expirationDate(tokenDecoded.getExpiresAt())
                .build();
    }
}

