package com.dera.AuthFortress.service;

import com.dera.AuthFortress.domain.enums.EmailTemplateName;
import jakarta.mail.MessagingException;

import java.util.Map;

public interface EmailService {
    void sendEmail(
            String to,
            String username,
            EmailTemplateName emailTemplate,
            String subject,
            Map<String, Object> properties
    ) throws MessagingException;
}
