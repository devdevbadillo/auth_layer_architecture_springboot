package com.david.auth_layer_architecture.business.service.implementation;

import java.util.List;

import com.david.auth_layer_architecture.common.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.common.utils.constants.routes.CredentialRoutes;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.david.auth_layer_architecture.common.utils.constants.errors.CredentialErrors;
import com.david.auth_layer_architecture.domain.entity.Credential;
import com.david.auth_layer_architecture.persistence.CredentialRepostory;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CredentialRepostory credentialRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Credential credential = this.findUser(username);
        List<SimpleGrantedAuthority> autorityList = List.of(
                new SimpleGrantedAuthority("ROLE_" + CommonConstants.ROLE_USER)
        );

        return new User(
                credential.getEmail(),
                credential.getPassword(),
                autorityList
        );
    }

    private Credential findUser(String email) throws UsernameNotFoundException {
        Credential credential = this.credentialRepository.getCredentialByEmail(email);
        if (credential == null) throw new UsernameNotFoundException(CredentialErrors.USER_NOT_REGISTERED);

        return credential;
    }
}
