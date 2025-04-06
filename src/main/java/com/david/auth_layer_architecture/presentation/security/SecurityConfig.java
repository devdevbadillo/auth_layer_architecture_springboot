package com.david.auth_layer_architecture.presentation.security;

import com.david.auth_layer_architecture.common.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.common.utils.constants.routes.AuthRoutes;
import com.david.auth_layer_architecture.common.utils.constants.routes.CredentialRoutes;
import com.david.auth_layer_architecture.presentation.security.filters.OAuth2SuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.david.auth_layer_architecture.common.utils.JwtUtil;
import com.david.auth_layer_architecture.presentation.security.filters.JwtValidateFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Value("${frontend.uri}")
    private String frontendUri;

    public SecurityConfig(JwtUtil jwtUtil, OAuth2SuccessHandler oAuth2SuccessHandler) {
        this.jwtUtil = jwtUtil;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
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
                       CommonConstants.PUBLIC_URL + AuthRoutes.SIGNIN_URL,
                       CommonConstants.PUBLIC_URL + CredentialRoutes.SIGNUP_URL,
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
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(( request, response, exception) -> {
                            response.sendRedirect(frontendUri + CommonConstants.SIGN_IN_FRONT_URL);
                        })
                )
            .addFilterBefore(new JwtValidateFilter(jwtUtil), BasicAuthenticationFilter.class);;
        return httpSecurity.build();
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfig(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of(frontendUri));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE", "PUT"));
        corsConfiguration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
