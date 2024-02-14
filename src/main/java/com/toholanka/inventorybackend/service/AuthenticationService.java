package com.toholanka.inventorybackend.service;

import com.toholanka.inventorybackend.model.AuthenticationToken;
import com.toholanka.inventorybackend.model.Users;
import com.toholanka.inventorybackend.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private TokenRepository tokenRepository;

    public void saveConfirmationToken(AuthenticationToken authenticationToken) {
        tokenRepository.save(authenticationToken);
    }

    public AuthenticationToken getToken(Users user) {
        return tokenRepository.findByUser(user);
    }

    public boolean validateTokenAndCheckAdminRole(String token) {
        AuthenticationToken authToken = tokenRepository.findByToken(token);
        if (authToken == null) {
            return false;
        }

        Users user = authToken.getUser();
        if (user == null) {
            return false;
        }

        return "ADMIN".equals(user.getRole());
    }

    public Users getUserByToken(String token){
        AuthenticationToken authToken = tokenRepository.findByToken(token);
        if (authToken == null) {
            return null;
        }

        return authToken.getUser();
    }

    public boolean validateTokenAndCheckRole(String token, String category){
        AuthenticationToken authToken = tokenRepository.findByToken(token);
        if (authToken == null) {
            return false;
        }

        Users user = authToken.getUser();
        if (user == null) {
            return false;
        }

        return category.equals(user.getRole());
    }
}