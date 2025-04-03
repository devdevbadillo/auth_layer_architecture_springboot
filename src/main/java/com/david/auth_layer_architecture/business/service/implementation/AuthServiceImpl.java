package com.david.auth_layer_architecture.business.service.implementation;

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
import com.david.auth_layer_architecture.common.utils.constants.CredentialConstants;
import com.david.auth_layer_architecture.domain.dto.request.SignInRequest;
import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService{

    private final UserDetailsServiceImpl userDetailsServiceImpl;    
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public MessageResponse signIn(SignInRequest signInRequest) throws BadCredentialsException {
        Authentication authentication = this.authenticate(signInRequest.getEmail(), signInRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtil.generateToken(authentication);
        return new MessageResponse(accessToken);
    }

    public Authentication authenticate(String username, String password) throws BadCredentialsException {
        try {
            UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) throw new BadCredentialsException(CredentialConstants.USERNAME_OR_PASSWORD_INCORRECT);

            return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }
}
