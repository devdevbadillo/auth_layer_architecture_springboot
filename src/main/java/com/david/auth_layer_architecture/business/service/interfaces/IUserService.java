package com.david.auth_layer_architecture.business.service.interfaces;

import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;

public interface IUserService {

    MessageResponse signOut(String accessTokenId);
}
