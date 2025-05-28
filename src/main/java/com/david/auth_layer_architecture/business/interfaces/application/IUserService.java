package com.david.auth_layer_architecture.business.interfaces.application;

import com.david.auth_layer_architecture.presentation.dto.response.MessageResponse;

public interface IUserService {

    MessageResponse signOut(String accessTokenId);
}
