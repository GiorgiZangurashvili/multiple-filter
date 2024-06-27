package org.example.multiplefilter.config.manager;

import lombok.RequiredArgsConstructor;
import org.example.multiplefilter.config.provider.ApiKeyAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class ApiKeyAuthenticationManager implements AuthenticationManager {
    private final String apiKey;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var provider = new ApiKeyAuthenticationProvider(apiKey);
        if (provider.supports(authentication.getClass())) {
            return provider.authenticate(authentication);
        } else {
            throw new BadCredentialsException("Oops!");
        }
    }
}
