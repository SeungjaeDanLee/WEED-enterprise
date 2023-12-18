package com.example.weed.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class W2001_JwtTokenUtil {

    @Value("${jwt.expiration}")
    private Long expiration;
    private final SecretKey accessSecret = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // 이후 코드는 변경하지 않고 secretKey로 사용하도록 수정
    public String generateToken(String email, int id) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("id", id);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(accessSecret, SignatureAlgorithm.HS512)
                .compact();
    }

    @Value("${jwt.refreshExpiration}")
    private Long refreshExpiration;
    private final SecretKey refreshSecret = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateRefreshToken(String email, int id) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("id", id);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(SignatureAlgorithm.HS512, refreshSecret)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Integer getUserIdFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("id", Integer.class));
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().setSigningKey(accessSecret).parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

}
