package com.david.auth_layer_architecture.business.services.impl.domain;

import com.david.auth_layer_architecture.business.services.interfaces.domain.IRefreshTokenService;
import com.david.auth_layer_architecture.application.mapper.RefreshTokenEntityMapper;
import com.david.auth_layer_architecture.domain.entity.AccessToken;
import com.david.auth_layer_architecture.domain.entity.Credential;
import com.david.auth_layer_architecture.domain.entity.RefreshToken;
import com.david.auth_layer_architecture.infrestructure.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RefreshTokenServiceImpl implements IRefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenEntityMapper refreshTokenEntityMapper;


    @Override
    public void saveRefreshToken(String refreshToken, Credential credential, AccessToken accessToken, String typeToken) {
        RefreshToken refreshTokenEntity = this.refreshTokenEntityMapper.toRefreshTokenEntity(refreshToken, credential, typeToken, accessToken);

        this.refreshTokenRepository.save(refreshTokenEntity);
    }

    @Override
    public RefreshToken findRefreshTokenByRefreshTokenId(String refreshTokenId) {
        return this.refreshTokenRepository.findRefreshTokenByRefreshTokenId(refreshTokenId);
    }

    @Override
    public RefreshToken findRefreshTokenByAccessToken(Long accessTokenId) {
        return this.refreshTokenRepository.findRefreshTokenByAccessTokenId(accessTokenId);
    }

    @Override
    public void deleteRefreshToken(AccessToken accessToken) {
        RefreshToken refreshToken = this.findRefreshTokenByAccessToken(accessToken.getId());
        this.refreshTokenRepository.delete(refreshToken);
    }
}
