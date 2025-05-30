package com.david.auth_layer_architecture.infrestructure.utils;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.david.auth_layer_architecture.presentation.messages.AuthMessages;
import com.david.auth_layer_architecture.domain.entity.Credential;
import com.david.auth_layer_architecture.infrestructure.utils.constants.CommonConstants;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtil {

    @Value("${jwt.key}")
    private String key;

    @Value("${jwt.user.generator}")
    private String userGenerator;

    public String generateToken(String username,  Date expirationToken, String typeToken) {
        Algorithm algorithm = Algorithm.HMAC256(this.key);

        return JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withClaim("authorities", "ROLE_" + CommonConstants.ROLE_USER)
                .withClaim("type", typeToken)
                .withIssuedAt(new Date())
                .withJWTId(UUID.randomUUID().toString())
                .withExpiresAt(expirationToken)
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);
    }

    public String generateToken(Date expirationToken, String typeToken) {
        Algorithm algorithm = Algorithm.HMAC256(this.key);

        return JWT.create()
                .withIssuer(this.userGenerator)
                .withClaim("type", typeToken)
                .withIssuedAt(new Date())
                .withJWTId(UUID.randomUUID().toString())
                .withExpiresAt(expirationToken)
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);
    }


    public DecodedJWT validateToken(String token) throws JWTVerificationException{
        try {

            Algorithm algorithm = Algorithm.HMAC256(this.key);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();

            return verifier.verify(token);
        } catch (TokenExpiredException ex){
            throw new JWTVerificationException(AuthMessages.TOKEN_EXPIRED_ERROR);
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException(AuthMessages.INVALID_TOKEN_ERROR);
        }

    }

    public String extractUser(DecodedJWT decodedJWT){
        return decodedJWT.getSubject();
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName){
        return decodedJWT.getClaim(claimName);
    }

    public Date calculateExpirationSecondsToken(Integer seconds){
        return new Date(System.currentTimeMillis() + (seconds * 1000));
    }

    public Date calculateExpirationMinutesToken(Integer minutes){
        return new Date(System.currentTimeMillis() + (minutes * 60 * 1000));
    }

    public Date calculateExpirationDaysToken(Integer days){
        return new Date(System.currentTimeMillis() + ( days * 24 * 60 * 60 * 1000));
    }

    public void validateTypeToken(DecodedJWT decodedJWT, String type) throws JWTVerificationException{
        String typeToken = this.getSpecificClaim(decodedJWT, "type").asString();
        if (!typeToken.equals(type)) {
            throw new JWTVerificationException(AuthMessages.INVALID_TOKEN_ERROR);
        }
    }

    public void handleInvalidToken(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }

    public String generateAccessToken(Credential credential, Integer expiration, String type) {
        Date expirationToken = calculateExpirationMinutesToken(expiration);
        return generateToken(credential.getEmail(), expirationToken, type);
    }

    public String generateRefreshToken(Credential credential, Integer expiration, String type) {
        Date expirationToken  = calculateExpirationDaysToken(expiration);
        return generateToken(credential.getEmail(), expirationToken, type);
    }
}