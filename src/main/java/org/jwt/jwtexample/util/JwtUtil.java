package org.jwt.jwtexample.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    private static final String SECRET = "mysecretkeyforjwtwebtokenexampleinspringbootandjava123098";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public String generateToken(String userName, Map<String, Object> claims) {
        return Jwts.builder()
                .header()
                    .type("JWT")
                    .and()
                .claims(claims)
                .subject(userName)
                .issuer("issuedbyaks")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, String userName) {
        return userName.equals(extractUserName(token)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date tokenExpirationDate = extractClaim(token, Claims::getExpiration);
        return tokenExpirationDate.before(new Date());
    }

}
