package com.window.system.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JwtUtils {
    private static final String DEFAULT_SECRET = "change-me-please-change-me-please-change-me";
    private static final long EXPIRE_MS = 1000L * 60 * 30; // 30 minutes

    private static Key key() {
        String secret = System.getenv("JWT_SECRET");
        if (secret == null || secret.length() < 32) {
            secret = DEFAULT_SECRET;
        }
        return Keys.hmacShaKeyFor(secret.getBytes());
    }


    public static Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
