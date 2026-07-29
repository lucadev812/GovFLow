package com.govflow.gov.flow.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;


    @Value("${jwt.expiration}")
    private Long expiration;



     private SecretKey getSigningKey() {
         return Keys.hmacShaKeyFor(
                 secret.getBytes(StandardCharsets.UTF_8)
         );
     }

     public String generateToken(UserDetails user) {

         return Jwts.builder()
                 .subject(user.getUsername())
                 .issuedAt(new Date())
                 .expiration(new Date(
                         System.currentTimeMillis() + expiration
                 ))
                 .signWith(getSigningKey())
                 .compact();
     }

     public String extractUsername(String token) {

         return Jwts.parser()
                 .verifyWith(getSigningKey())
                 .build()
                 .parseSignedClaims(token)
                 .getPayload()
                 .getSubject();
     }


     public boolean isTokenExpired(String token) {
         Date expiration = Jwts.parser()
                 .verifyWith(getSigningKey())
                 .build()
                 .parseSignedClaims(token)
                 .getPayload()
                 .getExpiration();
         return expiration.before(new Date());
     }
     public boolean isTokenValid(String token, UserDetails user) {
         String username = extractUsername(token);

         return username.equals(user.getUsername()) && !isTokenExpired(token);

     }

}
