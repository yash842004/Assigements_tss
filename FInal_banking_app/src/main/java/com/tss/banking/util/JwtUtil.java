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
@Component
public class JwtUtil {
    @Value("${app.jwt.secret:${jwt.secret:my-very-long-default-secret-key-that-is-at-least-32-characters}}")
    private String secret;
    @Value("${app.jwt.expiration:${jwt.expiration:259200000}}")
    private Long expiration;
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }
    public String extractUserType(String token) {
        return extractClaim(token, claims -> claims.get("userType", String.class));
    }
    public String extractAdminRole(String token) {
        return extractClaim(token, claims -> claims.get("adminRole", String.class));
    }
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
    public String generateToken(String username, Long userId, String userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userType", userType);
        return createToken(claims, username, expiration);
    }
    public String generateToken(String username, Long userId, String userType, String adminRole) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userType", userType);
        if (adminRole != null) {
            claims.put("adminRole", adminRole);
        }
        return createToken(claims, username, expiration);
    }
    private String createToken(Map<String, Object> claims, String subject, Long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    private Key getSignKey() {
        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(secret);
        } catch (IllegalArgumentException ex) {
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        if (keyBytes.length < 32) {
            byte[] padded = new byte[32];
            System.arraycopy(keyBytes, 0, padded, 0, Math.min(keyBytes.length, 32));
            for (int i = keyBytes.length; i < 32; i++) {
                padded[i] = (byte) (i * 31);
            }
            keyBytes = padded;
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public Date getExpirationDateFromToken(String token) {
        return extractExpiration(token);
    }
    public Long getUserIdFromToken(String token) {
        return extractUserId(token);
    }
    public String getUserEmailFromToken(String token) {
        return extractUsername(token);
    }
    public String getUserTypeFromToken(String token) {
        return extractUserType(token);
    }
	public static String getTokenFromRequest(HttpServletRequest httpRequest) {
		return null;
	}
}
