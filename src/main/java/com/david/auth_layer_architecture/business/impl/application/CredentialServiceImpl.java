package com.david.auth_layer_architecture.business.impl.application;

import com.david.auth_layer_architecture.business.interfaces.domain.ITokenService;
import com.david.auth_layer_architecture.common.exceptions.auth.HaveAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.common.exceptions.auth.UserNotVerifiedException;
import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.common.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.common.utils.constants.messages.AuthMessages;
import com.david.auth_layer_architecture.presentation.dto.response.PairTokenResponse;
import com.david.auth_layer_architecture.domain.entity.AccessToken;
import com.david.auth_layer_architecture.domain.events.DomainEventPublisher;
import com.david.auth_layer_architecture.domain.events.SendEmailEvent;
import com.david.auth_layer_architecture.domain.events.UserRegisteredEvent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.david.auth_layer_architecture.business.interfaces.application.ICredentialService;
import com.david.auth_layer_architecture.common.exceptions.credential.UserAlreadyExistException;
import com.david.auth_layer_architecture.common.utils.constants.messages.CredentialMessages;
import com.david.auth_layer_architecture.presentation.dto.response.MessageResponse;
import com.david.auth_layer_architecture.domain.entity.Credential;
import com.david.auth_layer_architecture.infrestructure.repository.CredentialRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CredentialServiceImpl implements ICredentialService{
    private final CredentialRepository  credentialRepository;
    private final DomainEventPublisher  domainEventPublisher;
    private final ITokenService         tokenService;

    @Override
    @Transactional(readOnly = false)
    public MessageResponse signUp(Credential credential,  Boolean isAccessWithOAuth2) throws UserAlreadyExistException {
        if ( this.getCredentialByEmail(credential.getEmail()) != null ) throw new UserAlreadyExistException(CredentialMessages.USER_ALREADY_EXISTS);

        credentialRepository.save(credential);

        if(!isAccessWithOAuth2) domainEventPublisher.publish(new UserRegisteredEvent(credential));

        return new MessageResponse(CredentialMessages.USER_CREATED_SUCCESSFULLY);
    }

    @Override
    public PairTokenResponse verifyAccount(String accessTokenId) {
        AccessToken accessToken = this.tokenService.getAccessToken(accessTokenId);
        Credential credential = accessToken.getCredential();

        credential.setIsVerified(true);
        this.credentialRepository.save(credential);

        this.tokenService.revokePairTokens(accessTokenId);

        return this.tokenService.generateAuthTokens(credential);
    }

    @Override
    @Transactional(readOnly = false)
    public MessageResponse refreshAccessToVerifyAccount(Credential credential, String refreshToken) {
        String accessToken = this.tokenService.saveAccessToken(credential, CommonConstants.TYPE_ACCESS_TOKEN_TO_VERIFY_ACCOUNT, CommonConstants.EXPIRATION_TOKEN_TO_VERIFY_ACCOUNT);

        domainEventPublisher.publish(new SendEmailEvent(credential, accessToken, CommonConstants.TYPE_ACCESS_TOKEN_TO_VERIFY_ACCOUNT, refreshToken));

        return new MessageResponse(CredentialMessages.SEND_EMAIL_VERIFY_ACCOUNT_SUCCESSFULLY);
    }

    @Override
    @Transactional(readOnly = false)
    public MessageResponse recoveryAccount(
            String email
    ) throws UserNotFoundException, UserNotVerifiedException, HaveAccessWithOAuth2Exception{
        Credential credential = this.isRegisteredUser(email);

        if(!credential.getIsVerified()) throw new UserNotVerifiedException(AuthMessages.USER_NOT_VERIFIED_ERROR);

        this.hasAccessWithOAuth2(credential);

        String accessToken = this.tokenService.saveAccessToken(credential, CommonConstants.TYPE_ACCESS_TOKEN_TO_CHANGE_PASSWORD,  CommonConstants.EXPIRATION_TOKEN_TO_CHANGE_PASSWORD);

        domainEventPublisher.publish(new SendEmailEvent(credential, accessToken, CommonConstants.TYPE_ACCESS_TOKEN_TO_CHANGE_PASSWORD));

        return new MessageResponse(CredentialMessages.RECOVERY_ACCOUNT_INSTRUCTIONS_SENT);
    }

    @Override
    public MessageResponse changePassword(Credential credential, String password, String accessTokenId) {
        credential.setPassword(password);
        credentialRepository.save(credential);

        this.tokenService.revokeAccessToken(accessTokenId);
        return new MessageResponse(CredentialMessages.CHANGE_PASSWORD_SUCCESSFULLY);
    }

    @Override
    public Credential getCredentialByEmail(String email) {
        return credentialRepository.getCredentialByEmail(email);
    }

    @Override
    public Credential isRegisteredUser(String email) throws UserNotFoundException{
        Credential credential = this.getCredentialByEmail(email);

        if (credential == null) throw new UserNotFoundException(CredentialMessages.USER_NOT_REGISTERED);

        return credential;
    }

    @Override
    public void hasAccessWithOAuth2(Credential credential) throws HaveAccessWithOAuth2Exception{
        if ( credential.getIsAccesOauth() ) throw new HaveAccessWithOAuth2Exception(AuthMessages.ACCESS_WITH_OAUTH2_ERROR);
    }
}
