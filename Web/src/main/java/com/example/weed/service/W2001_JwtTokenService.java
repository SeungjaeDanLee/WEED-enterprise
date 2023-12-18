package com.example.weed.service;

import com.example.weed.Util.W2001_JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
public class W2001_JwtTokenService {

    @Autowired
    private W2001_JwtTokenUtil jwtTokenUtil;

    public String generateToken(String email, int id) {
        return jwtTokenUtil.generateToken(email, id);
    }

    public String getEmailFromToken(String token) {
        return jwtTokenUtil.getEmailFromToken(token);
    }

    public Integer getUserIdFromToken(String token) {
        return jwtTokenUtil.getUserIdFromToken(token);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        return jwtTokenUtil.getClaimFromToken(token, claimsResolver);
    }

    public boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration != null && expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        try {
            return getClaimFromToken(token, Claims::getExpiration);
        } catch (Exception e) {
            return null;
        }
    }

}
