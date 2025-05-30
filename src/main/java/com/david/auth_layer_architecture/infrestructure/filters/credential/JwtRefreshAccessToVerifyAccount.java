package com.david.auth_layer_architecture.infrestructure.filters.credential;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.david.auth_layer_architecture.business.services.interfaces.domain.IAccessTokenService;
import com.david.auth_layer_architecture.business.services.interfaces.domain.IRefreshTokenService;
import com.david.auth_layer_architecture.infrestructure.utils.JwtUtil;
import com.david.auth_layer_architecture.infrestructure.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.presentation.messages.AuthMessages;
import com.david.auth_layer_architecture.infrestructure.utils.constants.routes.CredentialRoutes;
import com.david.auth_layer_architecture.domain.entity.RefreshToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Component
@AllArgsConstructor
public class JwtRefreshAccessToVerifyAccount extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final IRefreshTokenService refreshTokenService;
    private final IAccessTokenService accessTokenService;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        String path = request.getRequestURI();

        if (jwtToken != null && jwtToken.startsWith("Bearer ") && path.contains(CredentialRoutes.REFRESH_ACCESS_TO_VERIFY_ACCOUNT_URL)) {
            jwtToken = jwtToken.replace("Bearer ", "");

            try {
                DecodedJWT decodedJWT = jwtUtil.validateToken(jwtToken);
                jwtUtil.validateTypeToken(decodedJWT, CommonConstants.TYPE_REFRESH_TOKEN_TO_VERIFY_ACCOUNT);

                String refreshTokenId = jwtUtil.getSpecificClaim(decodedJWT, "jti").asString();
                RefreshToken refreshToken = this.refreshTokenService.findRefreshTokenByRefreshTokenId(refreshTokenId);

                if (refreshToken == null) throw new JWTVerificationException(AuthMessages.INVALID_TOKEN_ERROR);
                if(refreshToken.getAccessToken().getExpirationDate().compareTo(new Date()) > 0)  throw new JWTVerificationException(AuthMessages.INVALID_TOKEN_ERROR);

                request.setAttribute("refreshToken", jwtToken);
                request.setAttribute("credential", refreshToken.getCredential());
            } catch (JWTVerificationException ex) {
                this.jwtUtil.handleInvalidToken(response, ex.getMessage());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}
