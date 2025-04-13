package com.david.auth_layer_architecture.business.service.implementation;

import com.david.auth_layer_architecture.business.service.interfaces.IAccessTokenService;
import com.david.auth_layer_architecture.business.service.interfaces.ITypeTokenService;
import com.david.auth_layer_architecture.common.exceptions.accessToken.AlreadyHaveAccessTokenToChangePasswordException;
import com.david.auth_layer_architecture.common.mapper.AccessTokenEntityMapper;
import com.david.auth_layer_architecture.common.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.common.utils.constants.messages.CredentialMessages;
import com.david.auth_layer_architecture.domain.entity.AccessToken;
import com.david.auth_layer_architecture.domain.entity.Credential;
import com.david.auth_layer_architecture.domain.entity.TypeToken;
import com.david.auth_layer_architecture.persistence.AccessTokenRepository;
import com.david.auth_layer_architecture.persistence.TypeTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class AccessTokenServiceImpl implements IAccessTokenService {

    private final AccessTokenRepository accessTokenRepository;
    private final ITypeTokenService typeTokenService;
    private final AccessTokenEntityMapper accessTokenEntityMapper;


    @Override
    public void hasAccessTokenToChangePassword(Credential credential) throws AlreadyHaveAccessTokenToChangePasswordException {
        TypeToken typeToken = typeTokenService.getTypeToken(CommonConstants.TYPE_CHANGE_PASSWORD);
        AccessToken accessToken = accessTokenRepository.getTokenByCredentialAndTypeToken(credential, typeToken);

        if (accessToken != null && ( accessToken.getExpirationDate().compareTo(new Date()) ) > 0) {
            throw new AlreadyHaveAccessTokenToChangePasswordException(CredentialMessages.ALREADY_HAVE_ACCESS_TOKEN_TO_CHANGE_PASSWORD);
        };
    }

    @Override
    public void saveAccessTokenToChangePassword(String accessToken, Credential credential) {
        TypeToken typeToken = typeTokenService.getTypeToken(CommonConstants.TYPE_CHANGE_PASSWORD);
        AccessToken oldAccessToken = accessTokenRepository.getTokenByCredentialAndTypeToken(credential, typeToken);
        AccessToken newAccessToken = accessTokenEntityMapper.toTokenEntity(accessToken, credential, CommonConstants.TYPE_CHANGE_PASSWORD);

        if (oldAccessToken == null) {
            accessTokenRepository.save(newAccessToken);
            return;
        }

        this.setOldAccessTokenToChangePassword(oldAccessToken, newAccessToken);
        accessTokenRepository.save(oldAccessToken);
    }

    @Override
    public AccessToken saveAccessTokenToAccessApp(String accessToken, Credential credential) {
        AccessToken newAccessToken = accessTokenEntityMapper.toTokenEntity(accessToken, credential, CommonConstants.TYPE_ACCESS_TOKEN);
        return accessTokenRepository.save(newAccessToken);
    }

    @Override
    public AccessToken saveAccessTokenToAccessAppWithRefreshToken(AccessToken oldAccessToken, String accessToken) {
        AccessToken newAccessToken = accessTokenEntityMapper.toTokenEntity(accessToken, oldAccessToken.getCredential(), CommonConstants.TYPE_ACCESS_TOKEN);
        this.setOldAccessTokenToChangePassword(oldAccessToken, newAccessToken);
        return accessTokenRepository.save(oldAccessToken);
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
