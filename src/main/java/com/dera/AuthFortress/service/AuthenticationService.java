package com.dera.AuthFortress.service;

import com.dera.AuthFortress.payload.request.AuthenticationRequest;
import com.dera.AuthFortress.payload.request.RegistrationRequest;
import com.dera.AuthFortress.payload.response.AuthenticationResponse;
import jakarta.mail.MessagingException;

public interface AuthenticationService {
    void register(RegistrationRequest request) throws MessagingException;

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void activateAccount(String token) throws MessagingException;
}