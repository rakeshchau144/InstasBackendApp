package com.rakesh.instagramBackend.service;

import com.rakesh.instagramBackend.model.AuthenticationToken;
import com.rakesh.instagramBackend.model.User;
import com.rakesh.instagramBackend.repository.IAuthoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthoService {

    @Autowired
    IAuthoRepo authenticationRepo;
    public void saveAuthToken(AuthenticationToken authToken) {
        authenticationRepo.save(authToken);
    }

    public AuthenticationToken findFirstByUser(User user) {
        return authenticationRepo.findFirstByUser(user);
    }

    public void removeToken(AuthenticationToken token) {
        authenticationRepo.delete(token);

    }

    public boolean authenticate(String email, String token) {
        AuthenticationToken authToken = authenticationRepo.findFirstByTokenValue(token);

        if(authToken == null)
        {
            return false;
        }

        String tokenConnectedEmail = authToken.getUser().getUserEmail();

        return tokenConnectedEmail.equals(email);
    }
}
