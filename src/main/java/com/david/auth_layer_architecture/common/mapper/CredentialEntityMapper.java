package com.david.auth_layer_architecture.common.mapper;

import com.david.auth_layer_architecture.domain.dto.request.SignUpRequest;
import com.david.auth_layer_architecture.domain.entity.Credential;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CredentialEntityMapper {
    private final PasswordEncoder passwordEncoder;

    public CredentialEntityMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Credential toCredentialEntity(SignUpRequest signUpRequest){
        return Credential.builder()
                .email(signUpRequest.getEmail())
                .password(encodePassword(signUpRequest.getPassword()))
                .name(signUpRequest.getName())
                .isAccesOauth(false)
                .build();
    }

    public String encodePassword(String password){
        return passwordEncoder.encode(password);
    }
}

