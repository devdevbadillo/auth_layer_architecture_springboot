package com.david.auth_layer_architecture.business.service.implementation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.david.auth_layer_architecture.business.service.interfaces.ICredentialService;
import com.david.auth_layer_architecture.common.exceptions.credential.UserAlreadyExistException;
import com.david.auth_layer_architecture.common.utils.constants.CredentialConstants;
import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;
import com.david.auth_layer_architecture.domain.entity.Credential;
import com.david.auth_layer_architecture.persistence.CredentialRepostory;

@Service
@AllArgsConstructor
public class CredentialServiceImpl implements ICredentialService{

    private final CredentialRepostory credentialRepostory;

    @Override
    public MessageResponse signUp(Credential credential) throws UserAlreadyExistException {
        this.validateUniqueUser(credential.getEmail());

        credentialRepostory.save(credential);

        return new MessageResponse(CredentialConstants.USER_CREATED_SUCCESSFULLY);
    }

    private void validateUniqueUser(String email) throws UserAlreadyExistException{
        if (credentialRepostory.getCredentialByEmail(email) != null) throw new UserAlreadyExistException(CredentialConstants.USER_ALREADY_EXISTS);
    }
}
