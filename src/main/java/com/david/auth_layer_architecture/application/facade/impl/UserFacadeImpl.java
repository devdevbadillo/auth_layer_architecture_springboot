package com.david.auth_layer_architecture.application.facade.impl;

import com.david.auth_layer_architecture.application.facade.interfaces.IUserFacade;
import com.david.auth_layer_architecture.business.services.interfaces.application.IUserService;
import com.david.auth_layer_architecture.presentation.dto.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserFacadeImpl implements IUserFacade {

    private final IUserService userService;

    @Override
    public MessageResponse signOut(String accessTokenId) {
        return this.userService.signOut(accessTokenId);
    }
}
