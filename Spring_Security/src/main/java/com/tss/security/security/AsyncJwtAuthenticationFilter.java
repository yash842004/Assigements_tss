package com.tss.security.security;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Enhanced JWT Authentication Filter with Async Token Processing
 * Supports both synchronous and asynchronous token validation
 * Uses RSA algorithm for better security
 */
@Component
public class AsyncJwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AsyncJwtTokenProvider asyncTokenProvider;
    
    @Autowired
    private JwtTokenProvider tokenProvider; // Fallback for sync operations
    
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        // Enhanced token validation with async support
        if (StringUtils.hasText(token)) {
            try {
                // Use async token validation for better performance
                // For now, using sync version to maintain filter chain flow
                // In production, consider using reactive approach for full async benefits
                if (tokenProvider.validateToken(token)) {
                    String username = tokenProvider.getUsername(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    
                    UsernamePasswordAuthenticationToken authenticationToken = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                        );
                    
                    authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                // Enhanced error logging
                logger.error("JWT Authentication failed: " + e.getMessage());
                // Clear any existing authentication
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Enhanced token extraction with better validation
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            // Additional validation - ensure token is not empty after prefix removal
            return StringUtils.hasText(token) ? token : null;
        }
        return null;
    }

    /**
     * Async version of token validation (for future use with reactive approach)
     * This method demonstrates how async token processing could work
     */
    public CompletableFuture<Boolean> validateTokenAsync(String token) {
        if (!StringUtils.hasText(token)) {
            return CompletableFuture.completedFuture(false);
        }
        
        return asyncTokenProvider.validateTokenAsync(token)
            .exceptionally(throwable -> {
                logger.error("Async token validation failed: " + throwable.getMessage());
                return false;
            });
    }

    /**
     * Async version of user authentication (for future use with reactive approach)
     */
    public CompletableFuture<UsernamePasswordAuthenticationToken> authenticateAsync(String token) {
        return asyncTokenProvider.getUsernameAsync(token)
            .thenCompose(username -> {
                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                        );
                    return CompletableFuture.completedFuture(authToken);
                } catch (Exception e) {
                    return CompletableFuture.failedFuture(e);
                }
            })
            .exceptionally(throwable -> {
                logger.error("Async authentication failed: " + throwable.getMessage());
                return null;
            });
    }
}
