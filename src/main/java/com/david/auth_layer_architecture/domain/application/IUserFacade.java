package com.david.auth_layer_architecture.domain.application;

import com.david.auth_layer_architecture.presentation.dto.response.MessageResponse;

public interface IUserFacade {

    MessageResponse signOut(String accessTokenId);

}
