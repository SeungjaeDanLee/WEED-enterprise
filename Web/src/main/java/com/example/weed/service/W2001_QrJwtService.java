package com.example.weed.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;



import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class W2001_QrJwtService {

    @Autowired
    private W2001_JwtTokenService jwtTokenService;

    public String createAndReturnToken(String email, int id) {
        // 토큰 생성
        String token = jwtTokenService.generateToken(email, id);

        // 생성된 토큰 반환
        return token;
    }

    public String getEmailFromToken(String token) {
        // 토큰에서 이메일 추출
        return jwtTokenService.getEmailFromToken(token);
    }

    public Collection<? extends GrantedAuthority> getAuthoritiesFromToken(String token) {
        // 토큰에서 권한 정보 추출
        List<String> roles = jwtTokenService.getClaimFromToken(token, claims -> claims.get("roles", List.class));

        // 권한 정보를 SimpleGrantedAuthority로 매핑하여 반환
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public boolean validateToken(String token) {
        // 토큰 유효성 검증
        return !jwtTokenService.isTokenExpired(token);
    }



    public Integer getUserIdFromToken(String token) {
        // 토큰에서 사용자 ID 추출
        return jwtTokenService.getUserIdFromToken(token);
    }

    public boolean isTokenExpired(String token) {
        // 토큰 만료 여부 확인
        return jwtTokenService.isTokenExpired(token);
    }
}
