package com.david.auth_layer_architecture.common.utils;

import java.util.Date;
import java.util.UUID;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.david.auth_layer_architecture.common.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.common.utils.constants.errors.AuthErrors;
import com.david.auth_layer_architecture.common.utils.constants.routes.CredentialRoutes;
import org.springframework.beans.factory.annotation.Value;
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

    public String generateToken(String username, Integer minutes, Integer days) {
        Algorithm algorithm = Algorithm.HMAC256(this.key);

        Date expirationToken = minutes > 0 ? calculateExpirationAccessToken(minutes) : calculateExpirationRefreshToken(days);

        String typeToken = minutes > 0 ? "access_token" : "refresh_token";

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

    public DecodedJWT validateToken(String token) throws JWTVerificationException{
        try {

            Algorithm algorithm = Algorithm.HMAC256(this.key);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();

            return verifier.verify(token);
        } catch (TokenExpiredException ex){
            throw new JWTVerificationException(AuthErrors.TOKEN_EXPIRED_ERROR);
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException(AuthErrors.INVALID_TOKEN_ERROR);
        }

    }

    public String extractUser(DecodedJWT decodedJWT){
        return decodedJWT.getSubject();
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName){
        return decodedJWT.getClaim(claimName);
    }

    private Date calculateExpirationAccessToken(Integer minutes){
        return new Date(System.currentTimeMillis() + (minutes * 60 * 1000));
    }

    private Date calculateExpirationRefreshToken(Integer days){
        return new Date(System.currentTimeMillis() + ( days * 24 * 60 * 60 * 1000));
    }

    public void validateTypeToken(DecodedJWT decodedJWT, String type) throws JWTVerificationException{
        String typeToken = this.getSpecificClaim(decodedJWT, "type").asString();
        if (!typeToken.equals(type)) {
            throw new JWTVerificationException(AuthErrors.INVALID_TOKEN_ERROR);
        }
    }

}