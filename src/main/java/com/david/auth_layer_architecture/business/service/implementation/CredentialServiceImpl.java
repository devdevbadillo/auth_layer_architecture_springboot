package com.david.auth_layer_architecture.business.service.implementation;

import com.david.auth_layer_architecture.business.service.interfaces.IAccessTokenService;
import com.david.auth_layer_architecture.business.service.interfaces.IEmailService;
import com.david.auth_layer_architecture.common.exceptions.accessToken.AlreadyHaveAccessTokenToChangePasswordException;
import com.david.auth_layer_architecture.common.exceptions.auth.HaveAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.common.utils.JwtUtil;
import com.david.auth_layer_architecture.common.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.common.utils.constants.messages.AuthMessages;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.david.auth_layer_architecture.business.service.interfaces.ICredentialService;
import com.david.auth_layer_architecture.common.exceptions.credential.UserAlreadyExistException;
import com.david.auth_layer_architecture.common.utils.constants.messages.CredentialMessages;
import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;
import com.david.auth_layer_architecture.domain.entity.Credential;
import com.david.auth_layer_architecture.persistence.CredentialRepostory;

import java.util.Date;

@Service
@AllArgsConstructor
public class CredentialServiceImpl implements ICredentialService{

    private final CredentialRepostory credentialRepostory;
    private final IEmailService emailService;
    private final IAccessTokenService accessTokenService;
    private final JwtUtil jwtUtil;

    @Override
    public MessageResponse signUp(Credential credential) throws UserAlreadyExistException {
        this.isUniqueUser(credential.getEmail());

        credentialRepostory.save(credential);

        return new MessageResponse(CredentialMessages.USER_CREATED_SUCCESSFULLY);
    }

    @Override
    public MessageResponse recoveryAccount(
            String email
    ) throws UserNotFoundException, HaveAccessWithOAuth2Exception, MessagingException, AlreadyHaveAccessTokenToChangePasswordException {
        Credential credential = this.isRegisteredUser(email);
        this.hasAccessWithOAuth2(credential);
        this.accessTokenService.hasAccessTokenToChangePassword(credential);

        Date expirationAccessToken = jwtUtil.calculateExpirationMinutesToken(CommonConstants.EXPIRATION_CHANGE_PASSWORD_TOKEN_MINUTES);
        String accessToken = jwtUtil.generateToken(email, expirationAccessToken, CommonConstants.TYPE_CHANGE_PASSWORD );

        emailService.sendEmailRecoveryAccount(email, accessToken);

        this.accessTokenService.saveAccessTokenToChangePassword(accessToken, credential);

        return new MessageResponse(CredentialMessages.RECOVERY_ACCOUNT_INSTRUCTIONS_SENT);
    }

    @Override
    public MessageResponse changePassword(String password, String email, String accessTokenId) throws HaveAccessWithOAuth2Exception, UserNotFoundException {
        Credential credential = this.isRegisteredUser(email);
        this.hasAccessWithOAuth2(credential);

        credential.setPassword(password);
        credentialRepostory.save(credential);

        accessTokenService.deleteAccessToken(accessTokenId);
        return new MessageResponse(CredentialMessages.CHANGE_PASSWORD_SUCCESSFULLY);
    }


    @Override
    public Credential isRegisteredUser(String email) throws UserNotFoundException{
        Credential credential = credentialRepostory.getCredentialByEmail(email);
        if (credential == null) throw new UserNotFoundException(CredentialMessages.USER_NOT_REGISTERED);
        return credential;
    }

    @Override
    public void hasAccessWithOAuth2(Credential credential) throws HaveAccessWithOAuth2Exception{
        if ( credential.getIsAccesOauth() ){
            throw new HaveAccessWithOAuth2Exception(AuthMessages.ACCESS_WITH_OAUTH2_ERROR);
        }
    }

    private void isUniqueUser(String email) throws UserAlreadyExistException{
        if (credentialRepostory.getCredentialByEmail(email) != null) throw new UserAlreadyExistException(CredentialMessages.USER_ALREADY_EXISTS);
    }

}
