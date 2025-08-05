package com.smartpower;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter intercepts HTTP requests to extract and validate JWT tokens.
 * If a valid token is found, it sets the authentication in the SecurityContext.
 */
@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailServiceProvider userDetailServiceProvider;

    public JwtFilter(JwtService jwtService, UserDetailServiceProvider userDetailServiceProvider) {
        this.jwtService = jwtService;
        this.userDetailServiceProvider = userDetailServiceProvider;
    }

    /**
     * Intercepts each request to extract and validate JWT token, and sets authentication in context.
     *
     * @param request     incoming HTTP request
     * @param response    outgoing HTTP response
     * @param filterChain remaining filter chain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Skip if Authorization header is missing or not a Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract JWT token from header
        token = authHeader.substring(7);
        username = jwtService.extractUsername(token);

        // If username is present and context is not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails user = userDetailServiceProvider.loadUserByUsername(username);

            // Validate token
            if (jwtService.validateToken(token, user.getUsername())) {
                // Build and set authentication
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("JWT token validated and authentication set for user: {}", username);
            } else {
                log.warn("Invalid JWT token for user: {}", username);
            }
        }

        filterChain.doFilter(request, response);
    }
}

