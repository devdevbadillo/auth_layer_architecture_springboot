package com.david.auth_layer_architecture.infrestructure.filters.auth;

import java.io.IOException;
import java.util.Collection;

import com.david.auth_layer_architecture.infrestructure.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.presentation.messages.AuthMessages;
import com.david.auth_layer_architecture.domain.entity.AccessToken;
import com.david.auth_layer_architecture.infrestructure.repository.AccessTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.david.auth_layer_architecture.infrestructure.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;

@Component
@AllArgsConstructor
public class JwtAccessAppFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AccessTokenRepository accessTokenRepository;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        String path = request.getRequestURI();

        if (jwtToken != null && jwtToken.startsWith("Bearer ") && path.contains(CommonConstants.SECURE_URL)) {
            jwtToken = jwtToken.replace("Bearer ", "");

            try {
                DecodedJWT decodedJWT = jwtUtil.validateToken(jwtToken);
                jwtUtil.validateTypeToken(decodedJWT, CommonConstants.TYPE_ACCESS_TOKEN_TO_ACCESS_APP);

                String accessTokenId = jwtUtil.getSpecificClaim(decodedJWT, "jti").asString();
                AccessToken accessToken = this.accessTokenRepository.getTokenByAccessTokenId(accessTokenId);
                if( accessToken == null ) throw new JWTVerificationException(AuthMessages.INVALID_TOKEN_ERROR);

                String username = jwtUtil.extractUser(decodedJWT);
                String authorities = jwtUtil.getSpecificClaim(decodedJWT, "authorities").asString();

                Collection<? extends GrantedAuthority> authoritiesList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                SecurityContext context = SecurityContextHolder.getContext();
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authoritiesList);

                context.setAuthentication(authentication);
                request.setAttribute("accessTokenId", accessTokenId);
                SecurityContextHolder.setContext(context);
            } catch (JWTVerificationException ex) {
                this.jwtUtil.handleInvalidToken(response, ex.getMessage());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}