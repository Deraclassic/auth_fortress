package com.dera.AuthFortress.service.impl;

import com.dera.AuthFortress.domain.entities.AccessToken;
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
import com.dera.AuthFortress.service.AuthenticationService;
import com.dera.AuthFortress.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AccessTokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Value("${activation-host}")
    private String activateUrl;


    @Override
    public ResponseEntity<ResponseMessage> register(RegistrationRequest request) throws MessagingException {
        var user = TheUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(Collections.singleton(request.getRole() != null ? request.getRole() : UserRole.USER))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
        return ResponseEntity.ok(new ResponseMessage("Account registered successfully"));
    }


    private void sendValidationEmail(TheUser user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        Map<String, Object> properties = new HashMap<>();
        properties.put("confirmationUrl", activateUrl + "?token=" + newToken);
        properties.put("activation_code", newToken);

        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                "Account Activation",
                properties
        );
    }

    @Transactional
    private String generateAndSaveActivationToken(TheUser user) {
        String generatedToken = generateActivationCode(6);
        var token = AccessToken.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        try {
            tokenRepository.save(token);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        if (userRepository == null){
            log.error("User repository is null");
        }else {
            log.error("User repository is properly injected");
        }
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        TheUser user = (TheUser) auth.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("fullName", user.fullName());

        String jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .responseMessage("You have logged in successfully")
                .build();
    }

    @Override
    @Transactional
    public void activateAccount(String token) throws MessagingException {
        AccessToken saveToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid Token"));
        if (LocalDateTime.now().isAfter(saveToken.getExpiresAt())) {
            sendValidationEmail(saveToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been sent to the same email address");
        }
        TheUser user = userRepository.findById(saveToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        saveToken.setConfirmedAt(LocalDateTime.now());
        tokenRepository.save(saveToken);
    }
}
