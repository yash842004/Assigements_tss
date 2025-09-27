package com.tss.banking.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Utility class for JWT token operations
 */
@Component
public class JwtUtil {
    
    // Align keys with application.properties (app.jwt.*) and keep backward-compatible fallbacks
    @Value("${app.jwt.secret:${jwt.secret:my-very-long-default-secret-key-that-is-at-least-32-characters}}")
    private String secret;
    
    @Value("${app.jwt.expiration:${jwt.expiration:86400000}}") // 24 hours in milliseconds
    private Long expiration;
    
    @Value("${app.jwt.refresh-expiration:${jwt.refresh.expiration:604800000}}") // 7 days in milliseconds
    private Long refreshExpiration;
    
    /**
     * Extract username from token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * Extract user ID from token
     */
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }
    
    /**
     * Extract user type from token
     */
    public String extractUserType(String token) {
        return extractClaim(token, claims -> claims.get("userType", String.class));
    }
    
    /**
     * Extract admin role from token (for admin users)
     */
    public String extractAdminRole(String token) {
        return extractClaim(token, claims -> claims.get("adminRole", String.class));
    }
    
    /**
     * Extract expiration date from token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     * Extract a specific claim from token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Extract all claims from token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * Check if token is expired
     */
    public Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
    
    /**
     * Generate token for user
     */
    public String generateToken(String username, Long userId, String userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userType", userType);
        return createToken(claims, username, expiration);
    }
    
    /**
     * Generate token for admin with role
     */
    public String generateToken(String username, Long userId, String userType, String adminRole) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userType", userType);
        if (adminRole != null) {
            claims.put("adminRole", adminRole);
        }
        return createToken(claims, username, expiration);
    }
    
    /**
     * Generate refresh token
     */
    public String generateRefreshToken(String username, Long userId, String userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userType", userType);
        claims.put("tokenType", "REFRESH");
        return createToken(claims, username, refreshExpiration);
    }
    
    /**
     * Generate refresh token for admin with role
     */
    public String generateRefreshToken(String username, Long userId, String userType, String adminRole) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userType", userType);
        if (adminRole != null) {
            claims.put("adminRole", adminRole);
        }
        claims.put("tokenType", "REFRESH");
        return createToken(claims, username, refreshExpiration);
    }
    
    /**
     * Create token with claims
     */
    private String createToken(Map<String, Object> claims, String subject, Long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Validate token against user details
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    /**
     * Validate token
     */
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get signing key. Accepts either base64-encoded or plain text secrets.
     */
    private Key getSignKey() {
        byte[] keyBytes;
        try {
            // Try base64 decode first
            keyBytes = Decoders.BASE64.decode(secret);
        } catch (IllegalArgumentException ex) {
            // Fallback to raw bytes if not valid base64
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        // Ensure minimum key length for HS256 (>= 32 bytes)
        if (keyBytes.length < 32) {
            byte[] padded = new byte[32];
            System.arraycopy(keyBytes, 0, padded, 0, Math.min(keyBytes.length, 32));
            for (int i = keyBytes.length; i < 32; i++) {
                padded[i] = (byte) (i * 31); // simple deterministic padding
            }
            keyBytes = padded;
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    /**
     * Get token expiration time
     */
    public Date getExpirationDateFromToken(String token) {
        return extractExpiration(token);
    }
    
    /**
     * Check if token is refresh token
     */
    public Boolean isRefreshToken(String token) {
        try {
            String tokenType = extractClaim(token, claims -> claims.get("tokenType", String.class));
            return "REFRESH".equals(tokenType);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get user ID from token (alias for extractUserId)
     */
    public Long getUserIdFromToken(String token) {
        return extractUserId(token);
    }
    
    /**
     * Get user email from token (alias for extractUsername)
     */
    public String getUserEmailFromToken(String token) {
        return extractUsername(token);
    }
    
    /**
     * Get user type from token (alias for extractUserType)
     */
    public String getUserTypeFromToken(String token) {
        return extractUserType(token);
    }
    
    /**
     * Generate refresh token with two parameters
     */
    public String generateRefreshToken(Long userId, String userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userType", userType);
        claims.put("tokenType", "REFRESH");
        // When username is not available, use userId as subject
        return createToken(claims, String.valueOf(userId), refreshExpiration);
    }
    
    /**
     * Validate refresh token
     */
    public Boolean validateRefreshToken(String token) {
        try {
            return isRefreshToken(token) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get user ID from refresh token
     */
    public Long getUserIdFromRefreshToken(String refreshToken) {
        return extractUserId(refreshToken);
    }
    
    /**
     * Get user type from refresh token
     */
    public String getUserTypeFromRefreshToken(String refreshToken) {
        return extractUserType(refreshToken);
    }

	public static String getTokenFromRequest(HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		return null;
	}
}
