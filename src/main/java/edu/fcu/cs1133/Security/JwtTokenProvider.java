package edu.fcu.cs1133.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    // This is a placeholder implementation. You should implement your own JWT generation and validation logic here.
    public String generateToken(Authentication authentication) {
        return "dummy-jwt-token";
    }

    public boolean validateToken(String token) {
        return true;
    }

    public String getUsernameFromJWT(String token) {
        return "dummy-user";
    }
}
