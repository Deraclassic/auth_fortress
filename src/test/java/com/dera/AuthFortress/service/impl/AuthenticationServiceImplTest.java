package com.dera.AuthFortress.service.impl;

import com.dera.AuthFortress.domain.entities.TheUser;
import com.dera.AuthFortress.domain.enums.EmailTemplateName;
import com.dera.AuthFortress.domain.enums.UserRole;
import com.dera.AuthFortress.infrastructure.config.JwtService;
import com.dera.AuthFortress.payload.request.AuthenticationRequest;
import com.dera.AuthFortress.payload.request.RegistrationRequest;
import com.dera.AuthFortress.payload.response.AuthenticationResponse;
import com.dera.AuthFortress.payload.response.ResponseMessage;
import com.dera.AuthFortress.repository.AccessTokenRepository;
import com.dera.AuthFortress.repository.UserRepository;
import com.dera.AuthFortress.service.EmailService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccessTokenRepository tokenRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() throws MessagingException {
        RegistrationRequest request = new RegistrationRequest(
                "John",
                "Okon",
                "1234567890",
                "john.okon@gmail.com",
                "password",
                UserRole.USER
        );

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(TheUser.class))).thenReturn(new TheUser());
        doNothing().when(emailService).sendEmail(anyString(), anyString(), any(EmailTemplateName.class), anyString(), anyMap());

        ResponseEntity<ResponseMessage> response = authenticationService.register(request);

        assertNotNull(response);
        assertEquals("Account registered successfully", response.getBody().getMessage());
        verify(userRepository, times(1)).save(any(TheUser.class));
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), any(EmailTemplateName.class), anyString(), anyMap());
    }


    @Test
    void testAuthenticate() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("john.okon@gmail.com");
        request.setPassword("password");

        TheUser user = TheUser.builder().email(request.getEmail()).build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtService.generateToken(anyMap(), any(TheUser.class))).thenReturn("jwtToken");

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals("You have logged in successfully", response.getResponseMessage());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(anyMap(), any(TheUser.class));
    }
}
