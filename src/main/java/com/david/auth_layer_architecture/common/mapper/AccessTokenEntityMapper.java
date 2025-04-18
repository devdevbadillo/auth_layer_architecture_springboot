package com.david.auth_layer_architecture.common.mapper;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.david.auth_layer_architecture.business.service.interfaces.ITypeTokenService;
import com.david.auth_layer_architecture.common.utils.JwtUtil;
import com.david.auth_layer_architecture.domain.entity.Credential;
import com.david.auth_layer_architecture.domain.entity.AccessToken;
import com.david.auth_layer_architecture.domain.entity.TypeToken;
import com.david.auth_layer_architecture.persistence.TypeTokenRepository;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenEntityMapper {

    private  final JwtUtil jwtUtil;
    private final ITypeTokenService typeTokenService;

    public AccessTokenEntityMapper(JwtUtil jwtUtil, ITypeTokenService typeTokenService) {
        this.jwtUtil = jwtUtil;
        this.typeTokenService = typeTokenService;
    }

    public AccessToken toTokenEntity(String accessToken, Credential credential, String typeToken) {
        DecodedJWT tokenDecoded = this.jwtUtil.validateToken(accessToken);
        String tokenId = this.jwtUtil.getSpecificClaim(tokenDecoded, "jti").asString();

        return AccessToken.builder()
                .accessTokenId(tokenId)
                .credential(credential)
                .typeToken(this.typeTokenService.getTypeToken(typeToken))
                .creationDate(tokenDecoded.getIssuedAt())
                .expirationDate(tokenDecoded.getExpiresAt())
                .build();
    }


}
