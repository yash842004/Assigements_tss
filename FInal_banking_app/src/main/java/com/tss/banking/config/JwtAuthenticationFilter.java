package com.tss.banking.config;
import java.io.IOException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.tss.banking.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                logger.warn("Unable to extract username from JWT token: " + e.getMessage());
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                if (jwtUtil.validateToken(token)) {
                    Long userId = jwtUtil.extractUserId(token);
                    String userType = jwtUtil.extractUserType(token);
                    String adminRole = jwtUtil.extractAdminRole(token);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userType))
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    request.setAttribute("userId", userId);
                    request.setAttribute("userType", userType);
                    request.setAttribute("userEmail", username);
                    if (adminRole != null) {
                        request.setAttribute("adminRole", adminRole);
                    }
                }
            } catch (Exception e) {
                logger.warn("JWT token validation failed: " + e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}
