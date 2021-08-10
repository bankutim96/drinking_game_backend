package com.drinking.game.backend.security.service.impl;

import com.drinking.game.backend.errorhandling.ErrorCodes;
import com.drinking.game.backend.errorhandling.exception.InvalidTokenException;
import com.drinking.game.backend.security.domain.AuthUserDetails;
import com.drinking.game.backend.security.service.JwtTokenAuthenticationService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenAuthenticationServiceImpl implements JwtTokenAuthenticationService {

    private static final String TOKEN_PREFIX = "Bearer ";

    @Value("${drinking_game.security.token.secret}")
    private String secretKey;

    @Override
    public UsernamePasswordAuthenticationToken getAuthenticationFromToken(String token) {
        if (StringUtils.hasText(token)) {
            if (validateToken(token)) {
                var claims = Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                        .getBody();

                Collection<? extends GrantedAuthority> authorities = Collections.singleton(
                        new SimpleGrantedAuthority("ROLE_" + claims.get(TokenServiceImpl.ROLE_CLAIM).toString())
                );

                String userId = claims.get(TokenServiceImpl.USERID_CLAIM).toString();

                AuthUserDetails userDetails = AuthUserDetails.builder()
                        .userId(userId)
                        .username(claims.getAudience())
                        .authorities(authorities)
                        .build();

                return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            } else {
                throw new InvalidTokenException(ErrorCodes.INVALID_TOKEN);
            }
        }

        return null;
    }

    private boolean validateToken(String token) {
        String strippedToken = token.replace(TOKEN_PREFIX, "");
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(strippedToken);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace", e);
        }
        return false;
    }
}
