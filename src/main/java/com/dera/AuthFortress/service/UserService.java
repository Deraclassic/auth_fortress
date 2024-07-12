package com.dera.AuthFortress.service;

import com.dera.AuthFortress.payload.request.ChangePasswordRequest;

import java.security.Principal;

public interface UserService {
    public void changePassword(ChangePasswordRequest request, Principal connectedUser);
}
