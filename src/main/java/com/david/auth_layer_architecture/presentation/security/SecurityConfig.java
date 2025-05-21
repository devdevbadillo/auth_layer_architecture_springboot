package com.david.auth_layer_architecture.presentation.security;

import com.david.auth_layer_architecture.common.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.common.utils.constants.routes.AuthRoutes;
import com.david.auth_layer_architecture.common.utils.constants.routes.CredentialRoutes;
import com.david.auth_layer_architecture.persistence.AccessTokenRepository;
import com.david.auth_layer_architecture.persistence.RefreshTokenRepository;
import com.david.auth_layer_architecture.presentation.security.filters.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.david.auth_layer_architecture.common.utils.JwtUtil;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final OAuth2SuccessFilter oAuth2SuccessFilter;
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${uri.frontend}")
    private String frontUri;

    public SecurityConfig(
            JwtUtil jwtUtil,
            OAuth2SuccessFilter oAuth2SuccessFilter,
            AccessTokenRepository accessTokenRepository,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.jwtUtil = jwtUtil;
        this.oAuth2SuccessFilter = oAuth2SuccessFilter;
        this.accessTokenRepository = accessTokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .cors(c -> c.configurationSource(corsConfig()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(http -> {
                http.requestMatchers(
                        HttpMethod.POST,
                       CommonConstants.PUBLIC_URL + AuthRoutes.SIGN_IN_URL,
                       CommonConstants.PUBLIC_URL + CredentialRoutes.SIGN_UP_URL,
                       CommonConstants.PUBLIC_URL + AuthRoutes.REFRESH_TOKEN_URL
                    )
                    .permitAll();
                http.requestMatchers(HttpMethod.GET, CommonConstants.SECURE_URL + "/**").hasRole("USER");
                http.requestMatchers(HttpMethod.POST, CommonConstants.SECURE_URL + "/**").hasRole("USER");
                http.requestMatchers(HttpMethod.PUT, CommonConstants.SECURE_URL + "/**").hasRole("USER");
                http.requestMatchers(HttpMethod.PATCH, CommonConstants.SECURE_URL + "/**").hasRole("USER");
                http.requestMatchers(HttpMethod.DELETE, CommonConstants.SECURE_URL + "/**").hasRole("USER");

                // Allow OAuth2 endpoints
                http.requestMatchers("/oauth2/**", "/login/**").permitAll();

                http.anyRequest().permitAll();
            }).oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2SuccessFilter)
                        .failureHandler(( request, response, exception) -> {
                            response.sendRedirect(frontUri + CommonConstants.SIGN_IN_FRONT_URL);
                        })
                )
            .addFilterBefore(new JwtAccessAppFilter(jwtUtil, accessTokenRepository), BasicAuthenticationFilter.class)
            .addFilterBefore(new OAuth2ErrorFilter(jwtUtil), JwtAccessAppFilter.class)
            .addFilterBefore(new JwtChangePasswordFilter(jwtUtil, accessTokenRepository), JwtAccessAppFilter.class)
            .addFilterBefore(new JwtVerifyAccountFilter(jwtUtil, accessTokenRepository), JwtAccessAppFilter.class)
            .addFilterBefore(new JwtRefreshAccessToVerifyAccount(jwtUtil, refreshTokenRepository), JwtAccessAppFilter.class);

        return httpSecurity.build();
    }

    @Bean
    CorsConfigurationSource corsConfig(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of(frontUri));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE", "PUT"));
        corsConfiguration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
