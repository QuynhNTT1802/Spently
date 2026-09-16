package com.spently.config;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUtils {

    private static final SecretKey SECRET_KEY = Jwts.SIG.HS512.key().build();
    private final long expiration = Duration.ofMinutes(1500).toMillis();
    private final long expirationRefresh = Duration.ofDays(7).toMillis();
    private final String CLAIM_TYPE = "type";


    public String generateToken(CustomUserDetails userDetails,Boolean isRefreshToken) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_TYPE, isRefreshToken ? Constant.TYPE_REFRESH : Constant.TYPE_ACCESS);
        if(isRefreshToken){
            return Jwts.builder()
                    .claims(claims)
                    .subject(userDetails.getUsername())
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + expirationRefresh))
                    .signWith(SECRET_KEY)
                    .compact();
        }else{
            return Jwts.builder()
                    .claims(claims)
                    .subject(userDetails.getUsername())
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(SECRET_KEY)
                    .compact();
        }
    }

    // Get user from token
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getTokenTypeFromJwt(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(CLAIM_TYPE)
                .toString();
    }

    // validate token
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Get expiry date from token
    public LocalDateTime extractExpiredAt(String token){

        Date expiration = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime();
    }
}