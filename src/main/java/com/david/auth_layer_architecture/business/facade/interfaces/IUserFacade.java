package com.david.auth_layer_architecture.business.facade.interfaces;

import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;

public interface IUserFacade {

    MessageResponse signOut(String accessTokenId);

}
