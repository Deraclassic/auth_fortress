package com.dera.AuthFortress.infrastructure.controller;

import com.dera.AuthFortress.payload.request.AuthenticationRequest;
import com.dera.AuthFortress.payload.request.RegistrationRequest;
import com.dera.AuthFortress.payload.response.AuthenticationResponse;
import com.dera.AuthFortress.payload.response.RegisterResponse;
import com.dera.AuthFortress.payload.response.ResponseMessage;
import com.dera.AuthFortress.repository.UserRepository;
import com.dera.AuthFortress.service.AuthenticationService;
import com.dera.AuthFortress.service.impl.LogoutService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request) throws MessagingException {
        ResponseEntity<ResponseMessage> authenticationResponse = service.register(request);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        AuthenticationResponse response = service.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/activate-account")
    public ResponseEntity<?> activateAccount(@RequestParam String token) throws MessagingException {
        service.activateAccount(token);
        return ResponseEntity.ok("Account activated successfully");
    }

    private final LogoutService logoutService;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutService.logout(request, response, authentication);
        return ResponseEntity.ok("You have successfully logged out.");
    }
}
