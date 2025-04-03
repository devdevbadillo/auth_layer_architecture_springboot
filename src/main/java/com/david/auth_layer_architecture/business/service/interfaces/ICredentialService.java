package com.david.auth_layer_architecture.business.service.interfaces;

import com.david.auth_layer_architecture.common.exceptions.credential.UserAlreadyExistException;
import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;
import com.david.auth_layer_architecture.domain.entity.Credential;

public interface ICredentialService {
    public MessageResponse signUp(Credential credential) throws UserAlreadyExistException;
}
