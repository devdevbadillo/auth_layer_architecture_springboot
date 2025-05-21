package com.david.auth_layer_architecture.business.services;

import com.david.auth_layer_architecture.business.service.implementation.CredentialServiceImpl;
import com.david.auth_layer_architecture.business.service.interfaces.IAccessTokenService;
import com.david.auth_layer_architecture.business.service.interfaces.IEmailService;
import com.david.auth_layer_architecture.business.service.interfaces.IRefreshTokenService;
import com.david.auth_layer_architecture.common.exceptions.credential.UserAlreadyExistException;
import com.david.auth_layer_architecture.common.utils.JwtUtil;
import com.david.auth_layer_architecture.domain.entity.Credential;
import com.david.auth_layer_architecture.persistence.CredentialRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class CredentialServiceImplTest {
    // Repositories
    private CredentialRepository credentialRepository;
    // -------------------

    // Services
    @Mock
    private IEmailService emailService;

    @Mock
    private IAccessTokenService accessTokenService;

    @Mock
    private IRefreshTokenService refreshTokenService;
    // -------------------

    // Utils
    @Mock
    private JwtUtil jwtUtil;
    // -------------------

    @InjectMocks
    private CredentialServiceImpl credentialService;


    @Test
    void testSignUp_whenUserAlreadyExist_thenThrowException() throws UserAlreadyExistException, MessagingException {
        // Given
       when(credentialRepository.getCredentialByEmail(anyString())).thenReturn(new Credential());
       Credential credential = Credential.builder().email("email").password("password").name("name").build();

        // Then
        assertThrows(UserAlreadyExistException.class, () -> credentialService.signUp(credential, false));
    }

}
