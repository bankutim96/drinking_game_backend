package com.drinking.game.backend.security.service.impl;

import com.drinking.game.backend.domain.user.User;
import com.drinking.game.backend.errorhandling.ErrorCodes;
import com.drinking.game.backend.errorhandling.exception.DrinkingGameException;
import com.drinking.game.backend.repository.UserRepository;
import com.drinking.game.backend.security.domain.AuthUserDetails;
import com.drinking.game.backend.security.service.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            String userId = ((AuthUserDetails) authentication.getPrincipal()).getUserId();
            if (StringUtils.hasText(userId)) {
                return userRepository.findById(userId).orElseThrow(() -> new DrinkingGameException(ErrorCodes.NO_USER_IN_CONTEXT));
            }
        }
        throw new DrinkingGameException(ErrorCodes.NO_USER_IN_CONTEXT);
    }
}
