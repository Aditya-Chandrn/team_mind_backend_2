package com.capstone.mind.backend.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenStore {
    private final ConcurrentHashMap<String, String> tokens = new ConcurrentHashMap<>();

    public void storeToken(String token, String email) {
        tokens.put(token, email);
    }

    public boolean validateToken(String token) {
        return tokens.containsKey(token);
    }

    public void removeToken(String token) {
        tokens.remove(token);
    }

    public String getEmailFromToken(String token) {
        return tokens.get(token);
    }
}