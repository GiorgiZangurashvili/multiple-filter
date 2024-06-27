package org.example.multiplefilter.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.multiplefilter.config.authentication.ApiKeyAuthentication;
import org.example.multiplefilter.config.manager.ApiKeyAuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {
    private final String apiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var manager = new ApiKeyAuthenticationManager(apiKey);

        var requestKey = request.getHeader("x-api-key");

        if (requestKey == null || requestKey.equals("null")) {
            filterChain.doFilter(request, response);
        }

        var authentication = new ApiKeyAuthentication(requestKey);

        var a = manager.authenticate(authentication);

        if (a.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(a);
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        filterChain.doFilter(request, response);
    }
}
