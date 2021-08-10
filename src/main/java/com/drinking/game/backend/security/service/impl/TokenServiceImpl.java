package com.drinking.game.backend.security.service.impl;

import com.drinking.game.backend.domain.user.User;
import com.drinking.game.backend.security.service.TokenService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    @Value("${drinking_game.security.token.secret}")
    private String secretKey;

    public static final String USERID_CLAIM = "userid";
    public static final String ROLE_CLAIM = "role";
    private static final String TOKEN_ISSUER = "drinking_game";

    private static final String ACCESS_TOKEN_SUBJECT = "accesstoken";
    private static final String REFRESH_TOKEN_SUBJECT = "refreshtoken";

    private static final int ACCESS_TOKEN_TTL = 5 * 60 * 1000;
    private static final int REFRESH_TOKEN_TTL = 24 * 60 * 60 * 1000;

    @Override
    public String generateAccessToken(User user) {
        return generateTokenBase(user)
                .setSubject(ACCESS_TOKEN_SUBJECT)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_TTL))
                .compact();
    }

    @Override
    public String generateRefreshToken(User user) {
        return generateTokenBase(user)
                .setSubject(REFRESH_TOKEN_SUBJECT)
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_TTL))
                .compact();
    }

    private JwtBuilder generateTokenBase(User user) {
        return Jwts.builder()
                .claim(USERID_CLAIM, user.getId())
                .claim(ROLE_CLAIM, user.getRole())
                .setIssuer(TOKEN_ISSUER)
                .setAudience(user.getUsername())
                .signWith(SignatureAlgorithm.HS512, secretKey);
    }
}
