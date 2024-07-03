package com.dera.AuthFortress.infrastructure.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "AuthFortress",
                description = "User Authentication and Authorization System",
                version = "1.0",
                contact = @Contact(
                        name = "Chidera Uzoigwe",
                        email = "chiderauzoigwe2@gmail.com",
                        url = "https://github.com/Deraclassic"
                ),
                license = @License(
                        name = "AuthFortress Application",
                        url = "https://github.com/Deraclassic/auth_fortress"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "User Authentication and Authorization System",
                url = "https://github.com/Deraclassic/auth_fortress"
        ),
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
}

