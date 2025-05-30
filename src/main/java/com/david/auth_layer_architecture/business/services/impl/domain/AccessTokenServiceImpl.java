package com.david.auth_layer_architecture.business.services.impl.domain;

import com.david.auth_layer_architecture.business.services.interfaces.domain.IAccessTokenService;
import com.david.auth_layer_architecture.business.services.interfaces.domain.ITypeTokenService;
import com.david.auth_layer_architecture.application.mapper.AccessTokenEntityMapper;
import com.david.auth_layer_architecture.infrestructure.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.domain.entity.AccessToken;
import com.david.auth_layer_architecture.domain.entity.Credential;
import com.david.auth_layer_architecture.domain.entity.TypeToken;
import com.david.auth_layer_architecture.infrestructure.repository.AccessTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AccessTokenServiceImpl implements IAccessTokenService {

    private final AccessTokenRepository accessTokenRepository;
    private final ITypeTokenService typeTokenService;
    private final AccessTokenEntityMapper accessTokenEntityMapper;


    @Override
    public AccessToken saveAccessToken(String accessToken, Credential credential, String typeToken) {
        TypeToken type = typeTokenService.getTypeToken(typeToken);
        AccessToken oldAccessToken = accessTokenRepository.getTokenByCredentialAndTypeToken(credential, type);
        AccessToken newAccessToken = accessTokenEntityMapper.toTokenEntity(accessToken, credential, typeToken);

        if (oldAccessToken == null) return accessTokenRepository.save(newAccessToken);

        this.setOldAccessTokenToChangePassword(oldAccessToken, newAccessToken);
        return accessTokenRepository.save(oldAccessToken);
    }

    @Override
    public AccessToken saveAccessTokenToAccessApp(String accessToken, Credential credential) {
        AccessToken newAccessToken = accessTokenEntityMapper.toTokenEntity(accessToken, credential, CommonConstants.TYPE_ACCESS_TOKEN_TO_ACCESS_APP);
        return accessTokenRepository.save(newAccessToken);
    }

    @Override
    public void saveAccessTokenToAccessAppWithRefreshToken(AccessToken oldAccessToken, String accessToken) {
        AccessToken newAccessToken = accessTokenEntityMapper.toTokenEntity(accessToken, oldAccessToken.getCredential(), CommonConstants.TYPE_ACCESS_TOKEN_TO_ACCESS_APP);
        this.setOldAccessTokenToChangePassword(oldAccessToken, newAccessToken);
        accessTokenRepository.save(oldAccessToken);
    }

    @Override
    public AccessToken getTokenByAccessTokenId(String accessTokenId) {
        return this.accessTokenRepository.getTokenByAccessTokenId(accessTokenId);
    }

    @Override
    public void deleteAccessToken(String accessTokenId) {
        accessTokenRepository.delete( this.getTokenByAccessTokenId(accessTokenId) );
    }

    private void setOldAccessTokenToChangePassword(AccessToken oldAccessToken, AccessToken newAccessToken) {
        oldAccessToken.setAccessTokenId(newAccessToken.getAccessTokenId());
        oldAccessToken.setCreationDate(newAccessToken.getCreationDate());
        oldAccessToken.setExpirationDate(newAccessToken.getExpirationDate());
    }
}
