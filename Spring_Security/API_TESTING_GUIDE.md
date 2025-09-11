# Complete Postman API Testing Guide

## 🚀 Step-by-Step Postman Testing for Spring Security APIs

### Prerequisites
1. Install Postman from [https://www.postman.com/downloads/](https://www.postman.com/downloads/)
2. Make sure your Spring Boot application is running on `http://localhost:8080`
3. Ensure MySQL database is running with the `security_data` database created

---

## 📝 API Testing Sequence in Postman

### 1. **User Registration (Traditional Sync)**

**Steps in Postman:**
1. Create a new request
2. Set method to `POST`
3. Enter URL: `http://localhost:8080/api/auth/register`
4. Go to **Headers** tab and add:
   - Key: `Content-Type`
   - Value: `application/json`
5. Go to **Body** tab, select **raw**, choose **JSON** format
6. Enter this JSON:

```json
{
    "username": "testuser",
    "password": "password123",
    "role": "USER"
}
```

7. Click **Send**

**Expected Response:**
```json
{
    "userId": 1,
    "username": "testuser"
}
```

---

### 2. **User Login (Traditional Sync) - RSA JWT**

**Steps in Postman:**
1. Create a new request
2. Set method to `POST`
3. Enter URL: `http://localhost:8080/api/auth/login`
4. Add header: `Content-Type: application/json`
5. Body (JSON):

```json
{
    "username": "testuser",
    "password": "password123"
}
```

**Expected Response:**
```json
{
    "accessToken": "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTY5NDM2...",
    "tokenType": "Bearer"
}
```

**📌 IMPORTANT:** Copy the `accessToken` value! You'll need it for protected endpoints.

---

### 3. **Async User Registration**

**Steps in Postman:**
1. Method: `POST`
2. URL: `http://localhost:8080/api/auth/async/register`
3. Header: `Content-Type: application/json`
4. Body (JSON):

```json
{
    "username": "asyncuser",
    "password": "password123",
    "role": "USER"
}
```

---

### 4. **Async User Login - RSA JWT**

**Steps in Postman:**
1. Method: `POST`
2. URL: `http://localhost:8080/api/auth/async/login`
3. Header: `Content-Type: application/json`
4. Body (JSON):

```json
{
    "username": "asyncuser",
    "password": "password123"
}
```

---

### 5. **Async Token Validation**

**Steps in Postman:**
1. Method: `POST`
2. URL: `http://localhost:8080/api/auth/async/validate`
3. Go to **Headers** tab and add:
   - Key: `Authorization`
   - Value: `Bearer YOUR_ACCESS_TOKEN_HERE`
   - Key: `Content-Type`
   - Value: `application/json`

**Expected Response:** `Token is valid`

---

### 6. **Async Token Refresh**

**Steps in Postman:**
1. Method: `POST`
2. URL: `http://localhost:8080/api/auth/async/refresh`
3. Headers:
   - `Authorization: Bearer YOUR_ACCESS_TOKEN_HERE`
   - `Content-Type: application/json`

---

### 7. **Health Check**

**Steps in Postman:**
1. Method: `GET`
2. URL: `http://localhost:8080/api/auth/async/health`
3. No authentication required

**Expected Response:** `Async Auth Service is running with RSA-based JWT tokens`

---

## 🔧 Postman Pro Tips

### Setting Up Environment Variables

1. **Click the gear icon ⚙️** in top right corner
2. **Select "Manage Environments"**
3. **Click "Add"**
4. **Environment name:** `Spring Security Local`
5. **Add variables:**
   - Variable: `baseUrl`, Value: `http://localhost:8080`
   - Variable: `token`, Value: (leave empty for now)

### Auto-Save Token from Login

1. **In your login request, go to "Tests" tab**
2. **Add this JavaScript code:**

```javascript
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set("token", jsonData.accessToken);
    console.log("Token saved:", jsonData.accessToken);
}
```

3. **Now use `{{token}}` in Authorization headers:** `Bearer {{token}}`

### Using Variables in URLs

Instead of `http://localhost:8080`, use `{{baseUrl}}`

Example: `{{baseUrl}}/api/auth/login`

---

## 📋 Complete Testing Checklist

### ✅ Test Sequence:

1. **[ ] Register User** - `/api/auth/register`
2. **[ ] Login User** - `/api/auth/login` (save token)
3. **[ ] Test Async Register** - `/api/auth/async/register`
4. **[ ] Test Async Login** - `/api/auth/async/login`
5. **[ ] Validate Token** - `/api/auth/async/validate` (use saved token)
6. **[ ] Refresh Token** - `/api/auth/async/refresh` (use saved token)
7. **[ ] Health Check** - `/api/auth/async/health`

### ✅ Error Testing:

1. **[ ] Register with existing username** (should return 400)
2. **[ ] Login with wrong password** (should return 404)
3. **[ ] Use invalid token** (should return 400)
4. **[ ] Access without token** (should return 401)

---

## 🐛 Troubleshooting Common Issues

### Issue 1: Connection Refused
**Error:** `Could not get any response`
**Solution:** 
- Make sure Spring Boot app is running
- Check console for startup errors
- Verify port 8080 is not blocked

### Issue 2: 500 Internal Server Error
**Error:** Database connection issues
**Solution:**
- Ensure MySQL is running
- Check database `security_data` exists
- Verify credentials in application.properties

### Issue 3: 401 Unauthorized
**Error:** `Unauthorized`
**Solution:**
- Check Authorization header format: `Bearer YOUR_TOKEN`
- Ensure token is not expired
- Verify token was copied correctly

### Issue 4: 400 Bad Request
**Error:** Validation errors
**Solution:**
- Check JSON format in request body
- Verify all required fields are present
- Check for typos in field names

---

## 🎯 Expected Response Codes

| Endpoint | Method | Success | Error |
|----------|--------|---------|-------|
| `/api/auth/register` | POST | 200 | 400 (user exists) |
| `/api/auth/login` | POST | 200 | 404 (wrong credentials) |
| `/api/auth/async/register` | POST | 200 | 400 (user exists) |
| `/api/auth/async/login` | POST | 200 | 404 (wrong credentials) |
| `/api/auth/async/validate` | POST | 200 | 400 (invalid token) |
| `/api/auth/async/refresh` | POST | 200 | 400 (invalid token) |
| `/api/auth/async/health` | GET | 200 | - |

---

## 🚀 Quick Start Steps

### Step 1: Start Application
```bash
cd "c:\Users\yash.bhimani\Spring_Boot\Spring_Security"
mvnw.cmd spring-boot:run
```

### Step 2: Import Postman Collection
1. Copy the API calls above into Postman requests
2. Set up environment variables
3. Test in the order listed

### Step 3: Verify RSA Tokens
- RSA tokens are longer than HMAC tokens
- RSA tokens start with `eyJhbGciOiJSUzI1NiJ9`
- HMAC tokens start with `eyJhbGciOiJIUzI1NiJ9`

---

## 🎉 Success Indicators

**✅ Everything is working if you see:**
- Registration returns user details with userId
- Login returns RSA-based JWT token (RS256)
- Token validation returns "Token is valid"
- Async endpoints respond quickly
- Health check returns success message
- Protected endpoints accept valid tokens

**🔍 RSA Migration Success:**
- Tokens are longer (2048-bit RSA)
- Algorithm in JWT header is "RS256"
- No more shared secrets
- Better security with public/private key pairs

Happy Testing! 🚀
   - Install MySQL Server
   - Create database: `security_data`
   - Ensure MySQL is running on port 3306
   - Username: root, Password: root (as configured in application.properties)

2. **Java 17** (already configured in the project)

3. **API Testing Tool** (choose one):
   - Postman
   - Thunder Client (VS Code extension)
   - cURL commands
   - Any REST client

## Database Setup Commands

```sql
-- Connect to MySQL
mysql -u root -p

-- Create database
CREATE DATABASE security_data;

-- Use the database
USE security_data;

-- Create roles table (if not auto-created)
CREATE TABLE IF NOT EXISTS role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rolename VARCHAR(255) NOT NULL UNIQUE
);

-- Insert default roles
INSERT IGNORE INTO role (rolename) VALUES ('USER');
INSERT IGNORE INTO role (rolename) VALUES ('ADMIN');
```

## Application Architecture Overview

### Security Features Implemented:
1. **RSA-based JWT Authentication** (RS256 algorithm)
2. **Asynchronous Token Processing**
3. **Enhanced Security Configuration**
4. **Comprehensive Error Handling**

### Migration from HMAC to RSA:
- ❌ Old: HMAC (HS256) - Symmetric key (less secure)
- ✅ New: RSA (RS256) - Asymmetric keys (more secure)

### Key Components:
- `JwtTokenProvider`: RSA-based JWT operations
- `AsyncJwtTokenProvider`: Asynchronous JWT operations
- `RSAKeyConfig`: RSA key pair management
- `AsyncConfig`: Thread pool configuration
