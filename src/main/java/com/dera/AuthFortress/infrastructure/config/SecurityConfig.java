package com.dera.AuthFortress.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(
                                antMatcher(HttpMethod.POST, "/api/v1/auth/**"),
                                antMatcher(HttpMethod.GET, "/api/v1/auth/**"),
                                        antMatcher("/swagger-ui.html"),
                                        antMatcher("/webjars/**"),
                                        antMatcher("/swagger-ui/**"),
                                        antMatcher("/configuration/security"),
                                        antMatcher("/configuration/ui"),
                                        antMatcher("/swagger-resources/**"),
                                        antMatcher("/swagger-resources"),
                                        antMatcher("/v2/api-docs"),
                                        antMatcher("/v3/api-docs"),
                                        antMatcher("/v3/api-docs/**")

                                ).permitAll()
                                .requestMatchers(antMatcher("/admin/**")).hasAnyAuthority("ADMIN")
                                .requestMatchers(antMatcher("/user/**")).hasAnyAuthority("USER")
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
