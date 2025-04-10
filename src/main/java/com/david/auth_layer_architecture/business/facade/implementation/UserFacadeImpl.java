package com.david.auth_layer_architecture.business.facade.implementation;

import com.david.auth_layer_architecture.business.facade.interfaces.IUserFacade;
import com.david.auth_layer_architecture.business.service.interfaces.IUserService;
import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;
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
