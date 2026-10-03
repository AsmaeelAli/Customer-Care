package com.digitinary.customercare.security;

import com.digitinary.customercare.common.enums.Roles;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationMs;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expiration-ms}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }


    public String generateToken(UserDetails user) {
        String role = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_");


        return Jwts.builder()
                .subject(user.getUsername())                  // who is this token for?
                .claim("role", role)
                .expiration(new Date(System.currentTimeMillis() + expirationMs)) // when does it die?
                .signWith(key)                                // the stamp
                .compact();
    }


    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Roles extractRole(String token) {
        String roleStr = extractAllClaims(token).get("role", String.class);
        if (roleStr == null || roleStr.isBlank()) {
            throw new JwtException("Role claim is missing in token");
        }

        String cleanRole = roleStr.replace("ROLE_", "");
        return Roles.valueOf(cleanRole);
    }

    public long getExpirationMs() {
        return expirationMs;
    }
}
