package com.david.auth_layer_architecture.business.service.implementation;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.david.auth_layer_architecture.business.service.interfaces.IAccessTokenService;
import com.david.auth_layer_architecture.business.service.interfaces.IRefreshTokenService;
import com.david.auth_layer_architecture.common.exceptions.auth.HaveAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.common.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.common.utils.constants.messages.AuthMessages;
import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;
import com.david.auth_layer_architecture.domain.dto.response.SignInResponse;
import com.david.auth_layer_architecture.domain.entity.AccessToken;
import com.david.auth_layer_architecture.domain.entity.Credential;
import com.david.auth_layer_architecture.domain.entity.RefreshToken;
import com.david.auth_layer_architecture.persistence.CredentialRepostory;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.david.auth_layer_architecture.business.service.interfaces.IAuthService;
import com.david.auth_layer_architecture.common.utils.JwtUtil;
import com.david.auth_layer_architecture.common.utils.constants.messages.CredentialMessages;
import com.david.auth_layer_architecture.domain.dto.request.SignInRequest;
import java.util.Date;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService{

    private final UserDetailsServiceImpl userDetailsServiceImpl;    
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CredentialRepostory credentialRepostory;
    private final IAccessTokenService accessTokenService;
    private final IRefreshTokenService refreshTokenService;

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) throws BadCredentialsException, HaveAccessWithOAuth2Exception {
        Credential credential = this.validateAccess(signInRequest.getEmail());
        Authentication authentication = this.authenticate(signInRequest.getEmail(), signInRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Date expirationAccessToken = jwtUtil.calculateExpirationMinutesToken(CommonConstants.EXPIRATION_ACCESS_TOKEN_MINUTES);
        Date expirationRefreshToken = jwtUtil.calculateExpirationDaysToken(CommonConstants.EXPIRATION_REFRESH_TOKEN_DAYS);

        String accessToken = jwtUtil.generateToken(credential.getEmail(), expirationAccessToken, CommonConstants.TYPE_ACCESS_TOKEN );
        String refreshToken = jwtUtil.generateToken(credential.getEmail(), expirationRefreshToken, CommonConstants.TYPE_REFRESH_TOKEN );

        AccessToken accessTokenEntity = this.accessTokenService.saveAccessTokenToAccessApp(accessToken, credential);
        this.refreshTokenService.saveRefreshTokenToAccessApp(refreshToken, credential, accessTokenEntity);
        return new SignInResponse(accessToken, refreshToken);
    }

    @Override
    public SignInResponse refreshToken(String refreshToken) throws UserNotFoundException {
        try {
            DecodedJWT decodedJWT = jwtUtil.validateToken(refreshToken);
            jwtUtil.validateTypeToken(decodedJWT, CommonConstants.TYPE_REFRESH_TOKEN);

            RefreshToken refreshTokenEntity = this.refreshTokenService.findRefreshTokenByRefreshTokenId(decodedJWT.getClaim("jti").asString());

            String username = decodedJWT.getSubject();
            Date expirationAccessToken = jwtUtil.calculateExpirationMinutesToken(CommonConstants.EXPIRATION_ACCESS_TOKEN_MINUTES);
            String accessToken = jwtUtil.generateToken(username, expirationAccessToken, CommonConstants.TYPE_ACCESS_TOKEN );

            this.accessTokenService.saveAccessTokenToAccessAppWithRefreshToken(refreshTokenEntity.getAccessToken(), accessToken);
            return new SignInResponse(accessToken, refreshToken);
        } catch (UsernameNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        }
    }


    public Authentication authenticate(String username, String password) throws BadCredentialsException {
        try {
            UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) throw new BadCredentialsException(CredentialMessages.PASSWORD_INCORRECT);

            return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    private Credential validateAccess(String email) throws HaveAccessWithOAuth2Exception {
        Credential credential = credentialRepostory.getCredentialByEmail(email);
        if (credential != null && credential.getIsAccesOauth() ){
            throw new HaveAccessWithOAuth2Exception(AuthMessages.ACCESS_WITH_OAUTH2_ERROR);
        }
        return credential;
    }
}
