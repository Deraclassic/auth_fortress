package com.dera.AuthFortress.service;

import com.dera.AuthFortress.payload.request.AuthenticationRequest;
import com.dera.AuthFortress.payload.request.RegistrationRequest;
import com.dera.AuthFortress.payload.response.AuthenticationResponse;
import com.dera.AuthFortress.payload.response.ResponseMessage;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    public ResponseEntity<ResponseMessage> register(RegistrationRequest request) throws MessagingException;

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void activateAccount(String token) throws MessagingException;

}