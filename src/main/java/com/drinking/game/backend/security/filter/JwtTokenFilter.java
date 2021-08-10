package com.drinking.game.backend.security.filter;

import com.drinking.game.backend.security.service.JwtTokenAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtTokenFilter extends BasicAuthenticationFilter {

    private static final String HEADER_STRING = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtTokenAuthenticationService jwtTokenAuthenticationService;


    public JwtTokenFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint, JwtTokenAuthenticationService jwtTokenAuthenticationService) {
        super(authenticationManager, authenticationEntryPoint);
        this.jwtTokenAuthenticationService = jwtTokenAuthenticationService;
    }

    public JwtTokenFilter(AuthenticationManager authenticationManager, JwtTokenAuthenticationService jwtTokenAuthenticationService) {
        super(authenticationManager);
        this.jwtTokenAuthenticationService = jwtTokenAuthenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = resolveToken(request);

        return jwtTokenAuthenticationService.getAuthenticationFromToken(token);
    }

    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.replace(TOKEN_PREFIX, "");
        }

        return null;
    }
}
