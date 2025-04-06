package com.david.auth_layer_architecture.presentation.security.filters;

import com.david.auth_layer_architecture.common.utils.JwtUtil;
import com.david.auth_layer_architecture.common.utils.constants.CommonConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    public OAuth2SuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
        // TODO: Actualizar el security context y constant de expiración de tokens
        String login = oauthUser.getAttribute("login");
        String email = oauthUser.getAttribute("email");

        String username = login != null ? login : email;
        String accessToken = jwtUtil.generateToken(username, 60, 0);
        String refreshToken = jwtUtil.generateToken(username, 0, 7);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String redirectUrl =  CommonConstants.AUTH_SOCIAL_MEDIA_FRONT_URL  + "?accessToken=" + accessToken + "&refreshToken=" + refreshToken;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}