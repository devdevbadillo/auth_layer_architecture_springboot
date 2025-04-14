package com.david.auth_layer_architecture.business.service.implementation;

import com.david.auth_layer_architecture.business.service.interfaces.IAccessTokenService;
import com.david.auth_layer_architecture.business.service.interfaces.IEmailService;
import com.david.auth_layer_architecture.business.service.interfaces.IRefreshTokenService;
import com.david.auth_layer_architecture.common.exceptions.accessToken.AlreadyHaveAccessTokenToChangePasswordException;
import com.david.auth_layer_architecture.common.exceptions.auth.HaveAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.common.utils.JwtUtil;
import com.david.auth_layer_architecture.common.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.common.utils.constants.messages.AuthMessages;
import com.david.auth_layer_architecture.domain.dto.response.SignInResponse;
import com.david.auth_layer_architecture.domain.entity.AccessToken;
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
    private final IRefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    @Override
    public MessageResponse signUp(Credential credential,  Boolean isAccessWithOAuth2) throws UserAlreadyExistException, MessagingException {
        this.isUniqueUser(credential.getEmail());

        credentialRepostory.save(credential);

        if(!isAccessWithOAuth2) {
            Date expirationAccessToken = jwtUtil.calculateExpirationMinutesToken(CommonConstants.EXPIRATION_TOKEN_TO_VERIFY_ACCOUNT);
            String accessToken = jwtUtil.generateToken(credential.getEmail(), expirationAccessToken, CommonConstants.TYPE_VERIFY_ACCOUNT );

            Date expirationRefreshToken = jwtUtil.calculateExpirationDaysToken(CommonConstants.EXPIRATION_REFRESH_TOKEN_TO_VERIFY_ACCOUNT);
            String refreshToken = jwtUtil.generateToken(credential.getEmail(), expirationRefreshToken, CommonConstants.TYPE_REFRESH_TOKEN_TO_VERIFY_ACCOUNT );

            this.emailService.sendEmailVerifyAccount(credential.getEmail(), accessToken, refreshToken);

            AccessToken accessTokenEntity = this.accessTokenService.saveAccessTokenToVerifyAccount(accessToken, credential);
            this.refreshTokenService.saveRefreshTokenToAccessApp(refreshToken, credential, accessTokenEntity, CommonConstants.TYPE_REFRESH_TOKEN_TO_VERIFY_ACCOUNT);
        }

        return new MessageResponse(CredentialMessages.USER_CREATED_SUCCESSFULLY);
    }

    @Override
    public SignInResponse verifyAccount(String accessTokenId) {
        System.out.println(accessTokenId);
        AccessToken accessTokenToVerifyAccount = this.accessTokenService.getTokenByAccessTokenId(accessTokenId);
        Credential credential = accessTokenToVerifyAccount.getCredential();

        credential.setIsVerified(true);
        this.credentialRepostory.save(credential);

        this.refreshTokenService.deleteRefreshToken(accessTokenToVerifyAccount);

        Date expirationAccessToken = jwtUtil.calculateExpirationMinutesToken(CommonConstants.EXPIRATION_TOKEN_TO_ACCESS_APP);
        String accessToken = jwtUtil.generateToken(credential.getEmail(), expirationAccessToken, CommonConstants.TYPE_ACCESS_TOKEN );

        Date expirationRefreshToken = jwtUtil.calculateExpirationMinutesToken(CommonConstants.EXPIRATION_REFRESH_TOKEN_TO_ACCESS_APP);
        String refreshToken = jwtUtil.generateToken(credential.getEmail(), expirationRefreshToken, CommonConstants.TYPE_REFRESH_TOKEN );

        AccessToken accessTokenToApp = this.accessTokenService.saveAccessTokenToAccessApp(accessToken, credential);
        this.refreshTokenService.saveRefreshTokenToAccessApp(refreshToken, credential, accessTokenToApp, CommonConstants.TYPE_REFRESH_TOKEN);
        return new SignInResponse(accessToken, refreshToken);
    }

    @Override
    public MessageResponse recoveryAccount(
            String email
    ) throws UserNotFoundException, HaveAccessWithOAuth2Exception, MessagingException, AlreadyHaveAccessTokenToChangePasswordException {
        Credential credential = this.isRegisteredUser(email);
        this.hasAccessWithOAuth2(credential);
        this.accessTokenService.hasAccessTokenToChangePassword(credential);

        Date expirationAccessToken = jwtUtil.calculateExpirationMinutesToken(CommonConstants.EXPIRATION_TOKEN_TO_CHANGE_PASSWORD);
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
