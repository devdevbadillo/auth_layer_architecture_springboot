package com.david.auth_layer_architecture.application.facade.interfaces;

import com.david.auth_layer_architecture.presentation.dto.response.MessageResponse;

public interface IUserFacade {

    MessageResponse signOut(String accessTokenId);

}
