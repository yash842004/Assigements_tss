# Spring Security Migration: HMAC to RSA JWT with Async Support

## Overview
This project has been migrated from HMAC-based (HS256) JWT tokens to RSA-based (RS256) JWT tokens with asynchronous processing capabilities for enhanced security and performance.

## Key Changes Made

### 🔐 Security Enhancements

#### 1. **JWT Algorithm Migration: HMAC → RSA**
- **Before**: HS256 (HMAC with SHA-256) - Symmetric key algorithm
- **After**: RS256 (RSA with SHA-256) - Asymmetric key algorithm
- **Benefits**: 
  - Enhanced security with public/private key pairs
  - No shared secrets
  - Better key management
  - Improved scalability for microservices

#### 2. **Commented Out HMAC Implementation**
```java
// COMMENTED OUT: HMAC-based dependencies (HS256 algorithm)
// import javax.crypto.SecretKey;
// import io.jsonwebtoken.io.Decoders;
// import io.jsonwebtoken.security.Keys;

// COMMENTED OUT: HMAC-based secret (less secure, symmetric key)
// @Value("${app.jwt-secret}")
// private String jwtSecret;
```

### ⚡ Performance Improvements

#### 1. **Asynchronous Token Processing**
- Added `AsyncJwtTokenProvider` for non-blocking JWT operations
- Implemented async authentication filter
- Added thread pool configuration for optimal performance

#### 2. **Async Service Layer**
- `AsyncAuthService` interface with async methods
- `AuthServiceImp` now implements both sync and async operations
- CompletableFuture-based return types for async operations

### 🏗️ Architecture Changes

#### 1. **New Components Added**
```
src/main/java/com/tss/security/
├── config/
│   ├── AsyncConfig.java              # Async thread pool configuration
│   └── RSAKeyConfig.java             # RSA key pair management
├── security/
│   ├── AsyncJwtTokenProvider.java    # Async RSA-based JWT provider
│   └── AsyncJwtAuthenticationFilter.java # Enhanced auth filter
├── service/
│   └── AsyncAuthService.java         # Async service interface
└── controller/
    └── AsyncAuthController.java      # Async REST endpoints
```

#### 2. **Updated Components**
- `JwtTokenProvider.java` - Migrated to RSA algorithm
- `AuthServiceImp.java` - Added async methods
- `SecurityConfig.java` - Uses new async filter
- `AuthController.java` - Enhanced error handling

### 🐛 Bug Fixes

#### 1. **Date Import Issue Fixed**
```java
// FIXED: Using java.util.Date instead of java.sql.Date
import java.util.Date;
```

#### 2. **Enhanced Error Handling**
- Added proper exception handling for RSA operations
- Improved error messages for JWT validation
- Added SignatureException handling for RSA verification

#### 3. **Security Vulnerabilities Fixed**
- Removed hardcoded JWT secrets
- Implemented proper RSA key generation
- Added secure error responses

## Configuration Changes

### application.properties
```properties
# COMMENTED OUT: HMAC-based JWT secret (less secure)
# app.jwt-secret="tGagFgM1otrOscxuUN536lVFmrJb9/No8Khx/OBap5I="

# NEW: RSA-based JWT configuration (more secure)
app.jwt.rsa.key-size=2048
app.jwt-expiration-milliseconds=604800000

# Async Configuration
spring.task.execution.pool.core-size=4
spring.task.execution.pool.max-size=10
spring.task.execution.pool.queue-capacity=500
```

### pom.xml Dependencies Added
```xml
<!-- Spring Boot Async Support -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>

<!-- Bouncy Castle for RSA key generation -->
<dependency>
    <groupId>org.bouncycastle</groupId>
    <artifactId>bcprov-jdk15on</artifactId>
    <version>1.70</version>
</dependency>
```

## API Endpoints

### Traditional Sync Endpoints
- `POST /api/auth/login` - User login (RSA-based JWT)
- `POST /api/auth/register` - User registration

### New Async Endpoints
- `POST /api/auth/async/login` - Async user login
- `POST /api/auth/async/register` - Async user registration
- `POST /api/auth/async/validate` - Async token validation
- `POST /api/auth/async/refresh` - Async token refresh
- `GET /api/auth/async/health` - Health check

## Testing the Migration

### 1. Test RSA Token Generation
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password"}'
```

### 2. Test Async Operations
```bash
curl -X POST http://localhost:8080/api/auth/async/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password"}'
```

### 3. Test Token Validation
```bash
curl -X POST http://localhost:8080/api/auth/async/validate \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Performance Benefits

### Async Processing
- **Non-blocking operations**: JWT generation and validation don't block request threads
- **Better throughput**: Multiple requests can be processed concurrently
- **Resource efficiency**: Optimized thread pool usage

### RSA Algorithm Benefits
- **Enhanced security**: Asymmetric cryptography
- **Key management**: Separate public/private keys
- **Scalability**: Public key can be shared across services

## Security Improvements

### 1. RSA Key Pair Generation
- 2048-bit RSA keys (configurable)
- Automatic key generation on startup
- Secure random key generation

### 2. Enhanced Validation
- Signature verification with RSA public key
- Better error handling for different failure types
- Improved exception messages

### 3. Removed Security Risks
- No more shared secrets
- No hardcoded JWT secrets
- Better separation of concerns

## Migration Checklist

- ✅ Migrated from HMAC (HS256) to RSA (RS256)
- ✅ Added asynchronous token processing
- ✅ Fixed Date import issue
- ✅ Enhanced error handling
- ✅ Added RSA key configuration
- ✅ Implemented async service layer
- ✅ Created async REST endpoints
- ✅ Updated security configuration
- ✅ Added proper dependency management
- ✅ Commented out legacy HMAC code
- ✅ Enhanced application properties
- ✅ Improved thread pool configuration

## Future Enhancements

1. **Key Rotation**: Implement automatic RSA key rotation
2. **Key Storage**: Use external key management service (AWS KMS, Azure Key Vault)
3. **Reactive Support**: Full reactive programming with Spring WebFlux
4. **Monitoring**: Add metrics for async operations
5. **Caching**: Implement JWT token caching for better performance

## Troubleshooting

### Common Issues

1. **RSA Key Generation Error**
   - Ensure sufficient entropy for key generation
   - Check Java cryptography permissions

2. **Async Operation Timeout**
   - Adjust thread pool configuration
   - Monitor async task execution

3. **Token Validation Failures**
   - Verify RSA key pair consistency
   - Check token expiration settings

## Notes

- All legacy HMAC code has been commented out for reference
- Both sync and async endpoints are available for gradual migration
- RSA keys are generated at startup (in production, use external key management)
- Async operations use dedicated thread pools for better performance
