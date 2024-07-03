package com.dera.AuthFortress.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private Long id;
    private String responseCode;
    private String responseMessage;
    private String email;
    private String fullName;
    private String token;
}
