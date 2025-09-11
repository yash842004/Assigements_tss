package com.tss.security.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.tss.security.config.RSAKeyConfig;
import com.tss.security.exception.UserAPIException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

/**
 * Async JWT Token Provider using RSA algorithm (RS256)
 * Provides asynchronous token generation and validation for better performance
 */
@Component
public class AsyncJwtTokenProvider {

    @Autowired
    private RSAKeyConfig rsaKeyConfig;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    /**
     * Asynchronously generate JWT token using RS256 algorithm
     * @param authentication Spring Security Authentication object
     * @return CompletableFuture containing the generated JWT token
     */
    @Async("jwtTaskExecutor")
    public CompletableFuture<String> generateTokenAsync(Authentication authentication) {
        try {
            String username = authentication.getName();
            Date currentDate = new Date();
            Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

            RSAPrivateKey privateKey = rsaKeyConfig.rsaPrivateKey();

            String token = Jwts.builder()
                    .claims()
                        .subject(username)
                        .issuedAt(currentDate)
                        .expiration(expireDate)
                        .and()
                    .claim("role", authentication.getAuthorities())
                    .signWith(privateKey) // Using RSA private key for signing
                    .compact();

            return CompletableFuture.completedFuture(token);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(
                new UserAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating JWT token: " + e.getMessage())
            );
        }
    }

    /**
     * Synchronous token generation for backward compatibility
     * @param authentication Spring Security Authentication object
     * @return Generated JWT token
     */
    public String generateToken(Authentication authentication) {
        try {
            String username = authentication.getName();
            Date currentDate = new Date();
            Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

            RSAPrivateKey privateKey = rsaKeyConfig.rsaPrivateKey();

            return Jwts.builder()
                    .claims()
                        .subject(username)
                        .issuedAt(currentDate)
                        .expiration(expireDate)
                        .and()
                    .claim("role", authentication.getAuthorities())
                    .signWith(privateKey) // Using RSA private key for signing
                    .compact();
        } catch (Exception e) {
            throw new UserAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating JWT token: " + e.getMessage());
        }
    }

    /**
     * Asynchronously validate JWT token using RS256 algorithm
     * @param token JWT token to validate
     * @return CompletableFuture<Boolean> indicating if token is valid
     */
    @Async("jwtTaskExecutor")
    public CompletableFuture<Boolean> validateTokenAsync(String token) {
        try {
            RSAPublicKey publicKey = rsaKeyConfig.rsaPublicKey();
            
            Jwts.parser()
                .verifyWith(publicKey) // Using RSA public key for verification
                .build()
                .parse(token);
            
            return CompletableFuture.completedFuture(true);
        } catch (MalformedJwtException ex) {
            return CompletableFuture.failedFuture(
                new UserAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT token")
            );
        } catch (ExpiredJwtException ex) {
            return CompletableFuture.failedFuture(
                new UserAPIException(HttpStatus.BAD_REQUEST, "Expired JWT token")
            );
        } catch (UnsupportedJwtException ex) {
            return CompletableFuture.failedFuture(
                new UserAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token")
            );
        } catch (SignatureException ex) {
            return CompletableFuture.failedFuture(
                new UserAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT signature")
            );
        } catch (IllegalArgumentException ex) {
            return CompletableFuture.failedFuture(
                new UserAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty")
            );
        } catch (Exception e) {
            return CompletableFuture.failedFuture(
                new UserAPIException(HttpStatus.BAD_REQUEST, "Invalid Credentials")
            );
        }
    }

    /**
     * Synchronous token validation for backward compatibility
     * @param token JWT token to validate
     * @return boolean indicating if token is valid
     */
    public boolean validateToken(String token) {
        try {
            RSAPublicKey publicKey = rsaKeyConfig.rsaPublicKey();
            
            Jwts.parser()
                .verifyWith(publicKey) // Using RSA public key for verification
                .build()
                .parse(token);
            
            return true;
        } catch (MalformedJwtException ex) {
            throw new UserAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new UserAPIException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new UserAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        } catch (SignatureException ex) {
            throw new UserAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT signature");
        } catch (IllegalArgumentException ex) {
            throw new UserAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty");
        } catch (Exception e) {
            throw new UserAPIException(HttpStatus.BAD_REQUEST, "Invalid Credentials");
        }
    }

    /**
     * Asynchronously extract username from JWT token
     * @param token JWT token
     * @return CompletableFuture containing the username
     */
    @Async("jwtTaskExecutor")
    public CompletableFuture<String> getUsernameAsync(String token) {
        try {
            RSAPublicKey publicKey = rsaKeyConfig.rsaPublicKey();
            
            Claims claims = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

            String username = claims.getSubject();
            return CompletableFuture.completedFuture(username);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(
                new UserAPIException(HttpStatus.BAD_REQUEST, "Error extracting username from token: " + e.getMessage())
            );
        }
    }

    /**
     * Synchronous username extraction for backward compatibility
     * @param token JWT token
     * @return username extracted from token
     */
    public String getUsername(String token) {
        try {
            RSAPublicKey publicKey = rsaKeyConfig.rsaPublicKey();
            
            Claims claims = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

            return claims.getSubject();
        } catch (Exception e) {
            throw new UserAPIException(HttpStatus.BAD_REQUEST, "Error extracting username from token: " + e.getMessage());
        }
    }

    /**
     * Asynchronously extract all claims from JWT token
     * @param token JWT token
     * @return CompletableFuture containing Claims object
     */
    @Async("jwtTaskExecutor")
    public CompletableFuture<Claims> getClaimsAsync(String token) {
        try {
            RSAPublicKey publicKey = rsaKeyConfig.rsaPublicKey();
            
            Claims claims = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

            return CompletableFuture.completedFuture(claims);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(
                new UserAPIException(HttpStatus.BAD_REQUEST, "Error extracting claims from token: " + e.getMessage())
            );
        }
    }
}
