package com.david.auth_layer_architecture.infrestructure.filters.auth;

import com.david.auth_layer_architecture.business.services.interfaces.application.ICredentialService;
import com.david.auth_layer_architecture.business.services.interfaces.domain.ITokenService;
import com.david.auth_layer_architecture.domain.exceptions.credential.UserAlreadyExistException;
import com.david.auth_layer_architecture.infrestructure.utils.JwtUtil;
import com.david.auth_layer_architecture.infrestructure.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.presentation.dto.response.PairTokenResponse;
import com.david.auth_layer_architecture.presentation.messages.AuthMessages;
import com.david.auth_layer_architecture.domain.entity.Credential;
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
    private final ITokenService tokenService;

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

    private String handleValidEmail(String email, String name)  {
        try {
            registerNewUser(email, name);
            return createSuccessRedirectUrl(email);
        } catch (UserAlreadyExistException e) {
            return handleExistingUser(email);
        }
    }

    private void registerNewUser(String email, String name) throws UserAlreadyExistException {
        Credential credential = buildCredential(email, name);
        credentialService.signUp(credential, true);
    }

    private String handleExistingUser(String email)  {
        Credential credential = credentialService.getCredentialByEmail(email);

        return credential.getIsAccesOauth()
                ? createSuccessRedirectUrl(email)
                : createErrorRedirectUrl(AuthMessages.ACCESS_WITH_OAUTH2_ERROR);
    }

    private String createSuccessRedirectUrl(String email)  {
        Credential credential    =  credentialService.getCredentialByEmail(email);
        PairTokenResponse tokens = this.tokenService.generateAuthTokens(credential);

        return String.format( "%s?accessToken=%s&refreshToken=%s",
                CommonConstants.AUTH_SOCIAL_MEDIA_FRONT_URL, tokens.accessToken(), tokens.refreshToken());
    }

    private String createErrorRedirectUrl(String errorMessage) {
        String  encodedErrorMessage  = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        Date    expirationErrorToken = jwtUtil.calculateExpirationSecondsToken(CommonConstants.EXPIRATION_ERROR_TOKEN);
        String  errorToken           = jwtUtil.generateToken(expirationErrorToken, CommonConstants.TYPE_ERROR_TOKEN);

        return String.format( "%s?error=%s&errorToken=%s",
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