package org.mikhail.tilewebsite.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mikhail.tilewebsite.TurnstileValidationService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TurnstileLoginFilter extends OncePerRequestFilter {

    private final TurnstileValidationService validationService;
    private final SimpleUrlAuthenticationFailureHandler failureHandler =
            new SimpleUrlAuthenticationFailureHandler("/login?error=captcha");

    public TurnstileLoginFilter(TurnstileValidationService validationService) {
        this.validationService = validationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if ("POST".equalsIgnoreCase(request.getMethod()) && "/login".equals(request.getRequestURI())) {
            String token = request.getParameter("cf-turnstile-response");

            if (!validationService.isTokenValid(token)) {
                failureHandler.onAuthenticationFailure(request, response,
                        new BadCredentialsException("CAPTCHA validation failed"));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
