package com.example.ztpai.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class Jwt {

    String privateKey = "ztpaiztpaiztpai123ztpaiztpaiztpaiztpai123ztpaiztpaiztpaiztpai123ztpaiztpaiztpaiztpai123ztpai";

    public String generateToken(Authentication authentication){
        return Jwts.builder()
                .setSubject(authentication.getName())
                .signWith(Keys.hmacShaKeyFor(privateKey.getBytes()))
                .compact();
    }
}
