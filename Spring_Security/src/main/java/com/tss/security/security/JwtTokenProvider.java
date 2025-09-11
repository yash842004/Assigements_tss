package com.tss.security.security;

import java.security.interfaces.RSAPrivateKey; // Fixed: Using java.util.Date instead of java.sql.Date
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
 * JWT Token Provider using RSA algorithm (RS256) instead of HMAC (HS256)
 * Updated to use asymmetric cryptography for enhanced security
 * 
 * MIGRATION FROM HMAC TO RSA:
 * - Commented out HMAC-based secret key usage
 * - Implemented RSA private/public key pair for token signing/verification
 * - Fixed Date import issue (was using java.sql.Date instead of java.util.Date)
 */
@Component
public class JwtTokenProvider {

	// COMMENTED OUT: HMAC-based secret (less secure, symmetric key)
	// @Value("${app.jwt-secret}")
	// private String jwtSecret;

	@Autowired
	private RSAKeyConfig rsaKeyConfig;

	@Value("${app.jwt-expiration-milliseconds}")
	private long jwtExpirationDate;

	/**
	 * Generate JWT token using RSA private key (RS256 algorithm)
	 * UPDATED: Now uses RSA instead of HMAC for better security
	 */
	public String generateToken(Authentication authentication) {
		try {
			String username = authentication.getName();

			// FIXED: Using java.util.Date instead of java.sql.Date
			Date currentDate = new Date();
			Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

			// NEW: Using RSA private key for signing instead of HMAC secret
			RSAPrivateKey privateKey = rsaKeyConfig.rsaPrivateKey();

			String token = Jwts.builder()
					.claims()
						.subject(username)
						.issuedAt(currentDate)
						.expiration(expireDate)
						.and()
					.claim("role", authentication.getAuthorities())
					.signWith(privateKey) // RSA private key signing
					.compact();

			return token;
		} catch (Exception e) {
			throw new UserAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating JWT token: " + e.getMessage());
		}
	}

	/**
	 * Validate JWT token using RSA public key (RS256 algorithm)
	 * UPDATED: Now uses RSA public key for verification instead of HMAC secret
	 */
	public boolean validateToken(String token) {
		try {
			// NEW: Using RSA public key for verification instead of HMAC secret
			RSAPublicKey publicKey = rsaKeyConfig.rsaPublicKey();
			
			Jwts.parser()
				.verifyWith(publicKey) // RSA public key verification
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
			throw new UserAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
		} catch (Exception e) {
			throw new UserAPIException(HttpStatus.BAD_REQUEST, "Invalid Credentials");
		}
	}

	// COMMENTED OUT: HMAC-based key method (replaced with RSA keys)
	// private SecretKey key() {
	//     return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	// }
	
	/**
	 * Extract username from JWT token using RSA public key
	 * UPDATED: Now uses RSA public key for verification instead of HMAC secret
	 */
	public String getUsername(String token) {
		try {
			// NEW: Using RSA public key for verification instead of HMAC secret
			RSAPublicKey publicKey = rsaKeyConfig.rsaPublicKey();
			
			Claims claims = Jwts.parser()
				.verifyWith(publicKey) // RSA public key verification
				.build()
				.parseSignedClaims(token)
				.getPayload();

			String username = claims.getSubject();
			return username;
		} catch (Exception e) {
			throw new UserAPIException(HttpStatus.BAD_REQUEST, "Error extracting username from token: " + e.getMessage());
		}
	}

}
