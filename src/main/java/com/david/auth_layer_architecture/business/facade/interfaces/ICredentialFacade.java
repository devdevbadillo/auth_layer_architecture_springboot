package com.david.auth_layer_architecture.business.facade.interfaces;

import com.david.auth_layer_architecture.common.exceptions.credential.UserAlreadyExistException;
import com.david.auth_layer_architecture.domain.dto.request.SignUpRequest;
import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;

public interface ICredentialFacade {
   public MessageResponse signUp(SignUpRequest signUpRequest) throws UserAlreadyExistException;
}
