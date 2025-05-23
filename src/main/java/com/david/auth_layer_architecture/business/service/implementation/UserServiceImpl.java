package com.david.auth_layer_architecture.business.service.implementation;

import com.david.auth_layer_architecture.business.service.interfaces.IAccessTokenService;
import com.david.auth_layer_architecture.business.service.interfaces.IRefreshTokenService;
import com.david.auth_layer_architecture.business.service.interfaces.IUserService;
import com.david.auth_layer_architecture.common.utils.constants.messages.UserMessages;
import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;
import com.david.auth_layer_architecture.domain.entity.AccessToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IAccessTokenService accessTokenService;
    private final IRefreshTokenService refreshTokenService;

    @Override
    public MessageResponse signOut(String accessTokenId) {
        AccessToken accessToken = this.accessTokenService.getTokenByAccessTokenId(accessTokenId);

        this.refreshTokenService.deleteRefreshToken(accessToken);
        return new MessageResponse(UserMessages.SIGN_OUT_SUCCESSFULLY);
    }
}
