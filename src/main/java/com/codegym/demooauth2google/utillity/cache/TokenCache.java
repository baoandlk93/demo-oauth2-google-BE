package com.codegym.demooauth2google.utillity.cache;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public final class TokenCache {

    private final HashMap<String,String> tokenMap;

    private TokenCache(){
        tokenMap = new HashMap<>();
    }


    public void addToken(String username,String token) {
        tokenMap.put(username, token);
    }

    public String getToken(String username){
        return tokenMap.get(username);
    }

    public void removeToken(String username){
        tokenMap.remove(username);
    }
}
