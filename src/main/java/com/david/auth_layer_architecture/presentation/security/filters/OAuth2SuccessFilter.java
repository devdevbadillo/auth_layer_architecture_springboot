package com.david.auth_layer_architecture.presentation.security.filters;

import com.david.auth_layer_architecture.business.service.interfaces.IAccessTokenService;
import com.david.auth_layer_architecture.business.service.interfaces.ICredentialService;
import com.david.auth_layer_architecture.business.service.interfaces.IRefreshTokenService;
import com.david.auth_layer_architecture.common.exceptions.credential.UserAlreadyExistException;
import com.david.auth_layer_architecture.common.utils.JwtUtil;
import com.david.auth_layer_architecture.common.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.common.utils.constants.messages.AuthMessages;
import com.david.auth_layer_architecture.domain.entity.AccessToken;
import com.david.auth_layer_architecture.domain.entity.Credential;
import com.david.auth_layer_architecture.persistence.CredentialRepository;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@AllArgsConstructor
@Component
public class OAuth2SuccessFilter extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final ICredentialService credentialService;
    private final CredentialRepository credentialRepository;
    private final IRefreshTokenService refreshTokenService;
    private final IAccessTokenService accessTokenService;


    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        String redirectUrl = isValidEmail(email)
                ? handleValidEmail(email, name)
                : createErrorRedirectUrl(AuthMessages.OAUTH2_EMAIL_NULL_OR_INVALID_ERROR);

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private boolean isValidEmail(String email) {
        return email != null && !email.isEmpty();
    }

    private String handleValidEmail(String email, String name) {
        Date expirationAccessToken = jwtUtil.calculateExpirationMinutesToken(CommonConstants.EXPIRATION_TOKEN_TO_ACCESS_APP);
        Date expirationRefreshToken = jwtUtil.calculateExpirationDaysToken(CommonConstants.EXPIRATION_REFRESH_TOKEN_TO_ACCESS_APP);

        try {
            registerNewUser(email, name);
            return createSuccessRedirectUrl(email, expirationAccessToken, expirationRefreshToken);
        } catch (UserAlreadyExistException e) {
            return handleExistingUser(email, expirationAccessToken, expirationRefreshToken);
        } catch (MessagingException ex){
            return createErrorRedirectUrl(ex.getMessage());
        }
    }

    private void registerNewUser(String email, String name) throws UserAlreadyExistException, MessagingException {
        Credential credential = buildCredential(email, name);
        credentialService.signUp(credential, true);
    }

    private String handleExistingUser(String email, Date expirationAccessToken, Date expirationRefreshToken) {
        Credential credential = credentialRepository.getCredentialByEmail(email);

        return credential.getIsAccesOauth()
                ? createSuccessRedirectUrl(email, expirationAccessToken, expirationRefreshToken)
                : createErrorRedirectUrl(AuthMessages.ACCESS_WITH_OAUTH2_ERROR);
    }

    private String createSuccessRedirectUrl(String email, Date expirationAccessToken, Date expirationRefreshToken) {
        Credential credential = credentialRepository.getCredentialByEmail(email);
        String accessToken = jwtUtil.generateToken(email, expirationAccessToken, CommonConstants.TYPE_ACCESS_TOKEN);
        String refreshToken = jwtUtil.generateToken(email, expirationRefreshToken, CommonConstants.TYPE_REFRESH_TOKEN);

        AccessToken accessTokenEntity = this.accessTokenService.saveAccessTokenToAccessApp(accessToken, credential);
        this.refreshTokenService.saveRefreshToken(refreshToken, credential, accessTokenEntity, CommonConstants.TYPE_REFRESH_TOKEN);

        return String.format("%s?accessToken=%s&refreshToken=%s",
                CommonConstants.AUTH_SOCIAL_MEDIA_FRONT_URL, accessToken, refreshToken);
    }

    private String createErrorRedirectUrl(String errorMessage) {
        String encodedErrorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        Date expirationErrorToken = jwtUtil.calculateExpirationSecondsToken(CommonConstants.EXPIRATION_ERROR_TOKEN);
        String errorToken = jwtUtil.generateToken(expirationErrorToken, CommonConstants.TYPE_ERROR_TOKEN);

        return String.format("%s?error=%s&errorToken=%s",
                CommonConstants.AUTH_SOCIAL_MEDIA_FRONT_URL, encodedErrorMessage, errorToken);
    }

    private Credential buildCredential(String email, String name) {
        return Credential.builder()
                .email(email)
                .name(name)
                .isAccesOauth(true)
                .isVerified(true)
                .build();
    }
}