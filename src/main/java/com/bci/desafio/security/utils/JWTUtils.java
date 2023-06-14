package com.bci.desafio.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class JWTUtils {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.ttlMillis}")
    private long ttlMillis;

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        GrantedAuthority rol = userDetails.getAuthorities().stream().collect(Collectors.toList()).get(0);
        claims.put("rol", rol);
        return createToken(userDetails.getUsername(), claims);
    }

    private String createToken(String username, Map<String, Object> claims) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(ttlMillis)))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

    }

    public String getUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public LocalDateTime getExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(getAllClaims(token));
    }
    public Claims getAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails){
        final String username = getUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private Boolean isTokenExpired(String token) {
        return getExpirationDate(token).isBefore(LocalDateTime.now());
    }
}
