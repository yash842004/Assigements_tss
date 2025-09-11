# 🚀 Complete Step-by-Step Postman API Testing Guide
## Final Banking Application

### 📋 **Prerequisites**
1. ✅ Application is running on `http://localhost:8080`
2. ✅ MySQL database is connected and running
3. ✅ Postman is installed and ready
4. ✅ Import the provided Postman collection files

---

## 🚀 Quick Reference - API Summary

### AuthController Endpoints (4 total)
| Method | Endpoint | Purpose | Auth Required |
|--------|----------|---------|---------------|
| POST | `/api/auth/customer/login` | Customer authentication | ❌ No |
| POST | `/api/auth/admin/login` | Admin authentication | ❌ No |
| POST | `/api/auth/refresh` | Refresh JWT token | ✅ Refresh Token |
| POST | `/api/auth/logout` | User logout | ✅ Bearer Token |

### AdminController Endpoints (7 total)
| Method | Endpoint | Purpose | Auth Required |
|--------|----------|---------|---------------|
| POST | `/api/admin` | Create new admin | ✅ Bearer Token |
| GET | `/api/admin/{id}` | Get admin by ID | ✅ Bearer Token |
| GET | `/api/admin` | Get all admins (paginated) | ✅ Bearer Token |
| PUT | `/api/admin/{id}/roles/add/{role}` | Add role to admin | ✅ Bearer Token |
| PUT | `/api/admin/{id}/roles/remove/{role}` | Remove role from admin | ✅ Bearer Token |
| PUT | `/api/admin/{id}/activate` | Activate admin account | ✅ Bearer Token |
| PUT | `/api/admin/{id}/deactivate` | Deactivate admin account | ✅ Bearer Token |

## 📥 Import Instructions

### Option 1: Manual Import
1. **Copy Collection JSON**: Copy the content from `Auth_Admin_Postman_Collection.json`
2. **Import in Postman**: 
   - Open Postman → Import → Raw text
   - Paste the JSON content
   - Click Continue → Import

### Option 2: File Import
1. **Import File**: Postman → Import → Upload Files
2. **Select File**: Choose `Auth_Admin_Postman_Collection.json`
3. **Import**: Click Import button

### Environment Setup
Create a new environment with these variables:
```
base_url: http://localhost:8080
jwt_token: (auto-populated after login)
refresh_token: (auto-populated after login)  
user_id: (auto-populated after login)
```

---

## 🎯 **PHASE 1: Initial Setup & Customer Registration**

### **Step 1: Customer Registration**
**📍 Endpoint:** `POST {{baseUrl}}/api/customers/register`

**📝 Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe", 
  "email": "john.doe@example.com",
  "phone": "1234567890",
  "address": "123 Main Street, New York, NY 10001",
  "dateOfBirth": "1990-05-15",
  "password": "SecurePassword123!"
}
```

**✅ Expected Response:**
```json
{
  "success": true,
  "message": "Customer registered successfully",
  "data": {
    "customerId": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phoneNumber": "1234567890",
    "status": "ACTIVE",
    "registrationDate": "2025-09-10T10:30:00"
  }
}
```

**📝 Note:** Save the `customerId` for future requests.

---

### **Step 2: Customer Login**
**📍 Endpoint:** `POST {{baseUrl}}/api/auth/customer/login`

**📝 Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "SecurePassword123!"
}
```

**✅ Expected Response:**
```json
{
  "success": true,
  "message": "Customer logged in successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400,
    "userType": "CUSTOMER",
    "userId": 1,
    "email": "john.doe@example.com"
  }
}
```

**🔑 Important:** Copy the `accessToken` and set it as `{{customerToken}}` in Postman environment variables.

---

## 🎯 **PHASE 2: Account Management**

### **Step 3: Create Savings Account**
**📍 Endpoint:** `POST {{baseUrl}}/api/accounts`

**🔐 Headers:**
```
Authorization: Bearer {{customerToken}}
Content-Type: application/json
```

**📝 Request Body:**
```json
{
  "customerId": 1,
  "accountType": "SAVINGS",
  "initialDeposit": 5000.00
}
```

**✅ Expected Response:**
```json
{
  "success": true,
  "message": "Account created successfully",
  "data": {
    "accountId": 1,
    "accountNumber": "SAV123456789",
    "accountType": "SAVINGS",
    "balance": 5000.00,
    "status": "ACTIVE",
    "customerId": 1,
    "createdDate": "2025-09-10T10:35:00"
  }
}
```

**📝 Note:** Save the `accountId` and `accountNumber` for future transactions.

---

### **Step 4: Create Current Account**
**📍 Endpoint:** `POST {{baseUrl}}/api/accounts`

**🔐 Headers:**
```
Authorization: Bearer {{customerToken}}
Content-Type: application/json
```

**📝 Request Body:**
```json
{
  "customerId": 1,
  "accountType": "CURRENT", 
  "initialDeposit": 2000.00
}
```

**✅ Expected Response:**
```json
{
  "success": true,
  "message": "Account created successfully",
  "data": {
    "accountId": 2,
    "accountNumber": "CUR987654321",
    "accountType": "CURRENT",
    "balance": 2000.00,
    "status": "ACTIVE",
    "customerId": 1,
    "createdDate": "2025-09-10T10:40:00"
  }
}
```

---

### **Step 5: Check Account Balance**
**📍 Endpoint:** `GET {{baseUrl}}/api/accounts/1/balance`

**🔐 Headers:**
```
Authorization: Bearer {{customerToken}}
```

**✅ Expected Response:**
```json
{
  "success": true,
  "message": "Balance retrieved successfully",
  "data": 5000.00
}
```

---

## 🎯 **PHASE 3: Transaction Operations**

### **Step 6: Deposit Money**
**📍 Endpoint:** `POST {{baseUrl}}/api/transactions/deposit`

**🔐 Headers:**
```
Authorization: Bearer {{customerToken}}
Content-Type: application/json
```

**📝 Query Parameters:**
```
accountId=1
amount=1500.00
description=Salary deposit
```

**✅ Expected Response:**
```json
{
  "success": true,
  "message": "Deposit successful",
  "data": {
    "transactionId": 1,
    "accountId": 1,
    "accountNumber": "SAV123456789",
    "transactionType": "DEPOSIT",
    "amount": 1500.00,
    "description": "Salary deposit",
    "balanceAfter": 6500.00,
    "timestamp": "2025-09-10T10:45:00"
  }
}
```

---

### **Step 7: Withdraw Money**
**📍 Endpoint:** `POST {{baseUrl}}/api/transactions/withdraw`

**🔐 Headers:**
```
Authorization: Bearer {{customerToken}}
Content-Type: application/json
```

**📝 Query Parameters:**
```
accountId=1
amount=500.00
description=ATM withdrawal
```

**✅ Expected Response:**
```json
{
  "success": true,
  "message": "Withdrawal successful",
  "data": {
    "transactionId": 2,
    "accountId": 1,
    "accountNumber": "SAV123456789", 
    "transactionType": "WITHDRAWAL",
    "amount": 500.00,
    "description": "ATM withdrawal",
    "balanceAfter": 6000.00,
    "timestamp": "2025-09-10T10:50:00"
  }
}
```

---

### **Step 8: Transfer Money Between Accounts**
**📍 Endpoint:** `POST {{baseUrl}}/api/transactions/transfer`

**🔐 Headers:**
```
Authorization: Bearer {{customerToken}}
Content-Type: application/json
```

**📝 Request Body:**
```json
{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 1000.00,
  "description": "Transfer to current account"
}
```

**✅ Expected Response:**
```json
{
  "success": true,
  "message": "Transfer successful",
  "data": {
    "transferId": "TXF20250910001",
    "fromAccountId": 1,
    "fromAccountNumber": "SAV123456789",
    "toAccountId": 2,
    "toAccountNumber": "CUR987654321",
    "amount": 1000.00,
    "description": "Transfer to current account",
    "timestamp": "2025-09-10T10:55:00",
    "fromAccountBalance": 5000.00,
    "toAccountBalance": 3000.00,
    "status": "SUCCESS",
    "debitTransactionId": 3,
    "creditTransactionId": 4
  }
}
```

---

### **Step 9: View Transaction History**
**📍 Endpoint:** `GET {{baseUrl}}/api/transactions/account/1`

**🔐 Headers:**
```
Authorization: Bearer {{customerToken}}
```

**📝 Query Parameters:**
```
page=0
size=10
sort=transactionDate,desc
```

**✅ Expected Response:**
```json
{
  "success": true,
  "message": "Transactions retrieved successfully",
  "data": {
    "content": [
      {
        "transactionId": 3,
        "accountId": 1,
        "transactionType": "TRANSFER_OUT",
        "amount": 1000.00,
        "description": "Transfer to CUR987654321 - Transfer to current account",
        "balanceAfter": 5000.00,
        "timestamp": "2025-09-10T10:55:00"
      },
      {
        "transactionId": 2,
        "accountId": 1,
        "transactionType": "WITHDRAWAL",
        "amount": 500.00,
        "description": "ATM withdrawal",
        "balanceAfter": 6000.00,
        "timestamp": "2025-09-10T10:50:00"
      }
    ],
    "totalElements": 3,
    "totalPages": 1,
    "size": 10,
    "number": 0
  }
}
```

---

## 🎯 **PHASE 4: Admin Operations**

### **Step 10: Admin Login**
**📍 Endpoint:** `POST {{baseUrl}}/api/auth/admin/login`

**📝 Request Body:**
```json
{
  "email": "admin@bank.com",
  "password": "admin123"
}
```

**✅ Expected Response:**
```json
{
  "success": true,
  "message": "Admin logged in successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400,
    "userType": "ADMIN",
    "userId": 1,
    "email": "admin@bank.com"
  }
}
```

**🔑 Important:** Copy the `accessToken` and set it as `{{adminToken}}` in Postman environment variables.

---

### **Step 11: View All Customers (Admin)**
**📍 Endpoint:** `GET {{baseUrl}}/api/customers`

**🔐 Headers:**
```
Authorization: Bearer {{adminToken}}
```

**📝 Query Parameters:**
```
page=0
size=10
sort=id,asc
```

**✅ Expected Response:**
```json
{
  "success": true,
  "message": "Customers retrieved successfully",
  "data": {
    "content": [
      {
        "customerId": 1,
        "firstName": "John",
        "lastName": "Doe",
        "email": "john.doe@example.com",
        "phoneNumber": "1234567890",
        "status": "ACTIVE",
        "accountCount": 2,
        "totalBalance": 8000.00,
        "registrationDate": "2025-09-10T10:30:00"
      }
    ],
    "totalElements": 1,
    "totalPages": 1
  }
}
```

---

### **Step 12: Generate Financial Report (Admin)**
**📍 Endpoint:** `GET {{baseUrl}}/api/reports/financial-summary`

**🔐 Headers:**
```
Authorization: Bearer {{adminToken}}
```

**📝 Query Parameters:**
```
fromDate=2025-09-01
toDate=2025-09-30
```

**✅ Expected Response:**
```json
{
  "success": true,
  "message": "Financial summary generated successfully",
  "data": {
    "totalCustomers": 1,
    "totalAccounts": 2,
    "totalDeposits": 8500.00,
    "totalWithdrawals": 500.00,
    "totalTransfers": 1000.00,
    "netAmount": 8000.00,
    "transactionCount": 4,
    "averageTransactionAmount": 1750.00,
    "reportDate": "2025-09-10T11:00:00"
  }
}
```

---

## 🎯 **PHASE 5: Additional Testing Scenarios**

### **Step 13: Test Insufficient Funds**
**📍 Endpoint:** `POST {{baseUrl}}/api/transactions/withdraw`

**📝 Query Parameters:**
```
accountId=1
amount=10000.00
description=Large withdrawal
```

**❌ Expected Error Response:**
```json
{
  "success": false,
  "message": "Withdrawal failed: Insufficient balance for withdrawal",
  "data": null,
  "timestamp": "2025-09-10T11:05:00"
}
```

---

### **Step 14: Account Summary**
**📍 Endpoint:** `GET {{baseUrl}}/api/accounts/1/summary`

**🔐 Headers:**
```
Authorization: Bearer {{customerToken}}
```

**✅ Expected Response:**
```json
{
  "success": true,
  "message": "Account summary retrieved successfully",
  "data": {
    "accountId": 1,
    "accountNumber": "SAV123456789",
    "accountType": "SAVINGS",
    "currentBalance": 5000.00,
    "totalDeposits": 6500.00,
    "totalWithdrawals": 500.00,
    "totalTransfers": 1000.00,
    "transactionCount": 3,
    "lastTransactionDate": "2025-09-10T10:55:00",
    "accountAge": "0 days"
  }
}
```

---

### **Step 15: Register Second Customer**
**📍 Endpoint:** `POST {{baseUrl}}/api/customers/register`

**📝 Request Body:**
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@example.com",
  "phone": "9876543210",
  "address": "456 Oak Avenue, Los Angeles, CA 90001",
  "dateOfBirth": "1985-12-20",
  "password": "JaneSecure456!"
}
```

---

## 🔧 **Environment Variables Setup**

Set these variables in your Postman environment:

```json
{
  "baseUrl": "http://localhost:8080",
  "customerToken": "",
  "adminToken": "",
  "refreshToken": "",
  "customerId": "1",
  "accountId": "1",
  "secondCustomerId": "2",
  "secondAccountId": "2"
}
```

---

## 🚨 **Error Testing Scenarios**

### **Scenario 1: Invalid Authentication**
- Use expired or invalid token
- Expected: `401 Unauthorized`

### **Scenario 2: Insufficient Permissions**
- Customer trying to access admin endpoints
- Expected: `403 Forbidden`

### **Scenario 3: Invalid Data**
- Send malformed JSON or missing required fields
- Expected: `400 Bad Request`

### **Scenario 4: Resource Not Found**
- Try to access non-existent account/customer
- Expected: `404 Not Found`

---

## ✅ **Testing Checklist**

- [ ] Customer registration works
- [ ] Customer login returns valid JWT
- [ ] Account creation successful
- [ ] Deposit increases balance
- [ ] Withdrawal decreases balance
- [ ] Transfer between accounts works
- [ ] Transaction history accurate
- [ ] Admin login successful
- [ ] Admin can view customer data
- [ ] Reports generation works
- [ ] Error handling for invalid requests
- [ ] Insufficient funds validation
- [ ] Token expiration handling

---

## 📊 **Total API Endpoints Tested: 15+**

This comprehensive guide covers the core functionality of your banking application. Follow these steps sequentially for the best testing experience!

**🎯 Pro Tip:** Always check response status codes and save important IDs (customerId, accountId, tokens) as environment variables for easier testing flow.

---

## 🚨 **Troubleshooting Common Errors**

### **Error 1: Column 'phone_number' cannot be null**

**Problem:** Field name mismatch in JSON request body.

**❌ Incorrect Request:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "1234567890",    // ❌ WRONG FIELD NAME
  "address": "123 Main Street",
  "dateOfBirth": "1990-05-15",
  "password": "SecurePassword123!"
}
```

**✅ Correct Request:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "1234567890",           // ✅ CORRECT FIELD NAME
  "address": "123 Main Street",
  "dateOfBirth": "1990-05-15",
  "password": "SecurePassword123!"
}
```

**Solution:** Use `"phone"` instead of `"phoneNumber"` in your JSON request body.

### **Error 2: Validation Errors**

**Problem:** Missing required fields or invalid data format.

**Common Validation Issues:**
- ❌ Missing `firstName`, `lastName`, `email`, or `password`
- ❌ Invalid email format
- ❌ Password doesn't meet security requirements
- ❌ Phone number format invalid (use international format: +1234567890)
- ❌ Date of birth in future

### **Error 3: Duplicate Resource**

**Problem:** Customer with same email or phone already exists.

**Solution:** Use different email and phone number for registration.

### **Error 4: 403 Forbidden - Account Creation**

**Problem:** Missing authentication token when creating account.

**❌ Error Symptoms:**
- HTTP Status: `403 Forbidden`
- Endpoint: `POST /api/accounts`
- Missing Authorization header

**✅ Solution:**
1. **First complete customer login:**
   ```
   POST {{baseUrl}}/api/auth/customer/login
   Body: {
     "email": "john.doe@example.com", 
     "password": "SecurePassword123!"
   }
   ```

2. **Copy the `accessToken` from login response**

3. **Add Authorization header to account creation request:**
   ```
   Authorization: Bearer YOUR_JWT_TOKEN_HERE
   ```

**📝 Required Headers for Account Creation:**
```
Authorization: Bearer {{customerToken}}
Content-Type: application/json
```

**🔒 Protected Endpoints (Require Authentication):**
- `/api/accounts/*` - Account operations
- `/api/transactions/*` - Transaction operations  
- `/api/customers/{id}` - Customer profile operations

**🔓 Public Endpoints (No Authentication Required):**
- `/api/customers/register` - Customer registration
- `/api/auth/customer/login` - Customer login
- `/api/auth/admin/login` - Admin login

---

## ⚠️ **TROUBLESHOOTING: SSL/HTTPS Errors**

### Problem: SSL Error (EPROTO, WRONG_VERSION_NUMBER)
If you see SSL errors like:
```
Error: write EPROTO 1158840:error:1000007f:SSL routines:OPENSSL_internal:WRONG_VERSION_NUMBER
```

### Root Cause:
Your Spring Boot app runs on **HTTP** by default, but Postman is trying **HTTPS**.

### **Quick Fix:**
1. **Change URL Protocol**: Use `http://localhost:8080` instead of `https://localhost:8080`
2. **Update Environment**: Set `base_url = http://localhost:8080` (no 's' in http)
3. **Disable SSL Verification**: Postman Settings → Turn OFF "SSL certificate verification"

### **Correct URLs:**
✅ **Correct**: `http://localhost:8080/api/auth/admin/login`  
❌ **Wrong**: `https://localhost:8080/api/auth/admin/login`

---

## 🛠️ JWT Authentication Filter Fix

**If you were getting 403 Forbidden errors despite having a valid token, this has been fixed!**

### What was the problem?
The Spring Security configuration was requiring authentication for `/api/accounts` and other protected endpoints, but there was no JWT authentication filter configured to process the Bearer token from the Authorization header.

### What was fixed?
1. **Created JwtAuthenticationFilter**: A new filter that processes JWT tokens from the Authorization header
2. **Updated SecurityConfig**: Added the JWT filter to the security filter chain
3. **Token Processing**: The filter now properly extracts and validates JWT tokens, setting the security context

### Files Added/Modified:
- ✅ **NEW**: `JwtAuthenticationFilter.java` - Processes JWT tokens from Authorization header
- ✅ **UPDATED**: `SecurityConfig.java` - Added JWT filter to security chain

### How it works now:
1. **Request with Token**: When you send a request with `Authorization: Bearer <token>`
2. **Filter Processing**: JWT filter extracts and validates the token
3. **Security Context**: Sets authentication in Spring Security context
4. **Access Granted**: Request proceeds to the controller with proper authentication

### No More 403 Errors!
Your account creation and other authenticated requests should now work properly with the Bearer token.

---

## Step-by-Step Fix for Account Creation After Login:

**✅ You've completed login successfully! Now follow these steps:**

1. **Copy Your JWT Token:**
   - From your login response, copy the entire `token` value
   - Example: `eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOi...` (the full long string)

2. **Set Environment Variable:**
   - In Postman, go to your environment settings
   - Set `customerToken` = `YOUR_COPIED_TOKEN`
   - Or manually paste it in the Authorization Bearer Token field

3. **Add Request Body:**
   - Go to the `Body` tab in your account creation request
   - Select `raw` and `JSON`
   - Add this JSON:
   ```json
   {
     "customerId": 2,
     "accountType": "SAVINGS",
     "initialDeposit": 5000.00
   }
   ```

4. **Verify Headers:**
   - `Authorization: Bearer {{customerToken}}`
   - `Content-Type: application/json`

5. **Send the Request!**

**Expected Successful Response:**
```json
{
  "success": true,
  "message": "Account created successfully",
  "data": {
    "accountId": 1,
    "accountNumber": "SAV123456789",
    "accountType": "SAVINGS",
    "balance": 5000.00,
    "status": "ACTIVE",
    "customerId": 2,
    "createdDate": "2025-09-10T10:35:00"
  }
}
```

---

## 🔐 AuthController API Endpoints

### 1. Customer Login
**Method:** `POST`  
**URL:** `{{base_url}}/api/auth/customer/login`  
**Headers:** 
```json
Content-Type: application/json
```
**Body (JSON):**
```json
{
    "email": "customer@example.com",
    "password": "password123"
}
```
**Expected Response:**
```json
{
    "success": true,
    "message": "Customer logged in successfully",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9...",
        "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
        "tokenType": "Bearer",
        "userId": 1,
        "email": "customer@example.com",
        "userType": "CUSTOMER",
        "expiresAt": "2025-09-12 10:30:45"
    }
}
```

### 2. Admin Login
**Method:** `POST`  
**URL:** `{{base_url}}/api/auth/admin/login`  
**Headers:** 
```json
Content-Type: application/json
```
**Body (JSON):**
```json
{
    "email": "admin@example.com",
    "password": "admin123"
}
```
**Expected Response:**
```json
{
    "success": true,
    "message": "Admin logged in successfully",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9...",
        "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
        "tokenType": "Bearer",
        "userId": 1,
        "email": "admin@example.com",
        "userType": "ADMIN",
        "expiresAt": "2025-09-12 10:30:45"
    }
}
```

### 3. Refresh Token
**Method:** `POST`  
**URL:** `{{base_url}}/api/auth/refresh`  
**Headers:** 
```json
Refresh-Token: {{refresh_token}}
```
**Expected Response:**
```json
{
    "success": true,
    "message": "Token refreshed successfully",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9...",
        "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
        "tokenType": "Bearer",
        "userId": 1,
        "userType": "CUSTOMER"
    }
}
```

### 4. Logout
**Method:** `POST`  
**URL:** `{{base_url}}/api/auth/logout`  
**Headers:** 
```json
Authorization: Bearer {{jwt_token}}
```
**Expected Response:**
```json
{
    "success": true,
    "message": "Logged out successfully",
    "data": "OK"
}
```

---

## 👑 AdminController API Endpoints

### 1. Create New Admin
**Method:** `POST`  
**URL:** `{{base_url}}/api/admin`  
**Headers:** 
```json
Authorization: Bearer {{jwt_token}}
Content-Type: application/json
```
**Body (JSON):**
```json
{
    "fullName": "John Doe",
    "email": "john.admin@example.com",
    "password": "securePassword123",
    "roles": ["ADMIN"]
}
```
**Available Roles:** `SUPER_ADMIN`, `ADMIN`

**Expected Response:**
```json
{
    "success": true,
    "message": "Admin created successfully",
    "data": {
        "id": 2,
        "fullName": "John Doe",
        "email": "john.admin@example.com",
        "roles": ["ADMIN"],
        "active": true,
        "createdAt": "2025-09-11T10:30:45"
    }
}
```

### 2. Get Admin by ID
**Method:** `GET`  
**URL:** `{{base_url}}/api/admin/1`  
**Headers:** 
```json
Authorization: Bearer {{jwt_token}}
```
**Expected Response:**
```json
{
    "success": true,
    "message": "Admin retrieved successfully",
    "data": {
        "id": 1,
        "fullName": "Super Admin",
        "email": "super@admin.com",
        "roles": ["SUPER_ADMIN"],
        "active": true,
        "createdAt": "2025-09-10T08:00:00"
    }
}
```

### 3. Get All Admins (Paginated)
**Method:** `GET`  
**URL:** `{{base_url}}/api/admin?page=0&size=10&sort=createdAt,desc`  
**Headers:** 
```json
Authorization: Bearer {{jwt_token}}
```
**Query Parameters:**
- `page`: Page number (default: 0)
- `size`: Page size (default: 10)
- `sort`: Sort criteria (e.g., `createdAt,desc`, `fullName,asc`)

**Expected Response:**
```json
{
    "success": true,
    "message": "Admins retrieved successfully",
    "data": {
        "content": [
            {
                "id": 1,
                "fullName": "Super Admin",
                "email": "super@admin.com",
                "roles": ["SUPER_ADMIN"],
                "active": true
            }
        ],
        "totalElements": 1,
        "totalPages": 1,
        "size": 10,
        "number": 0
    }
}
```

### 4. Add Role to Admin
**Method:** `PUT`  
**URL:** `{{base_url}}/api/admin/2/roles/add/SUPER_ADMIN`  
**Headers:** 
```json
Authorization: Bearer {{jwt_token}}
```
**Path Parameters:**
- `id`: Admin ID
- `role`: Role to add (`SUPER_ADMIN` or `ADMIN`)

**Expected Response:**
```json
{
    "success": true,
    "message": "Role added successfully",
    "data": {
        "id": 2,
        "fullName": "John Doe",
        "email": "john.admin@example.com",
        "roles": ["ADMIN", "SUPER_ADMIN"],
        "active": true
    }
}
```

### 5. Remove Role from Admin
**Method:** `PUT`  
**URL:** `{{base_url}}/api/admin/2/roles/remove/ADMIN`  
**Headers:** 
```json
Authorization: Bearer {{jwt_token}}
```
**Path Parameters:**
- `id`: Admin ID
- `role`: Role to remove (`SUPER_ADMIN` or `ADMIN`)

**Expected Response:**
```json
{
    "success": true,
    "message": "Role removed successfully",
    "data": {
        "id": 2,
        "fullName": "John Doe",
        "email": "john.admin@example.com",
        "roles": ["SUPER_ADMIN"],
        "active": true
    }
}
```

### 6. Activate Admin
**Method:** `PUT`  
**URL:** `{{base_url}}/api/admin/2/activate`  
**Headers:** 
```json
Authorization: Bearer {{jwt_token}}
```

**Expected Response:**
```json
{
    "success": true,
    "message": "Admin activated successfully",
    "data": {
        "id": 2,
        "fullName": "John Doe",
        "email": "john.admin@example.com",
        "roles": ["ADMIN"],
        "active": true
    }
}
```

### 7. Deactivate Admin
**Method:** `PUT`  
**URL:** `{{base_url}}/api/admin/2/deactivate`  
**Headers:** 
```json
Authorization: Bearer {{jwt_token}}
```

**Expected Response:**
```json
{
    "success": true,
    "message": "Admin deactivated successfully",
    "data": "OK"
}
```

---

## 📝 Postman Environment Variables Setup

Create these variables in your Postman environment:

```json
{
    "base_url": "http://localhost:8080",
    "jwt_token": "{{token_from_login_response}}",
    "refresh_token": "{{refresh_token_from_login_response}}",
    "user_id": "{{user_id_from_login_response}}"
}
```

## 🔄 Testing Flow Recommendations

### 1. Authentication Flow:
```
1. Admin Login → 2. Create Admin → 3. Get All Admins → 4. Logout
```

### 2. Role Management Flow  
```
1. Admin Login → 2. Add Role → 3. Remove Role → 4. Get Admin by ID
```

### 3. Admin Status Management
```
1. Admin Login → 2. Deactivate Admin → 3. Activate Admin → 4. Get Admin by ID
```

### 4. Token Management
```
1. Customer Login → 2. Use Token → 3. Refresh Token → 4. Use New Token → 5. Logout
```

---

## ⚠️ **TROUBLESHOOTING: 400 Bad Request - Invalid Admin Credentials**

### Problem: "Authentication failed: Invalid admin credentials"
```json
{
    "success": false,
    "message": "Authentication failed: Invalid admin credentials",
    "data": null,
    "errorCode": null
}
```

### Root Cause:
The admin user you're trying to login with doesn't exist in the database.

### **Solution: Default Admin Created**
I've added a DataLoader that automatically creates a default admin user when the application starts.

### **Default Admin Credentials:**
```json
{
    "email": "admin@bank.com",
    "password": "admin123"
}
```

### **Steps to Fix:**
1. **Restart your Spring Boot application**
2. **Check the console logs** - you should see:
   ```
   Creating default admin user...
   Default admin created successfully with email: admin@bank.com
   Login credentials - Email: admin@bank.com | Password: admin123
   ```
3. **Use the correct credentials** in Postman:
   - ✅ **Email**: `admin@bank.com`
   - ✅ **Password**: `admin123`

### **Alternative: Create Admin via Database**
If you prefer to create the admin manually:
```sql
-- For MySQL database
INSERT INTO admins (first_name, last_name, email, password_hash, is_active, created_date, last_updated) 
VALUES ('Super', 'Admin', 'admin@bank.com', '$2a$10$encodedPasswordHash', true, NOW(), NOW());

INSERT INTO admin_roles (admin_id, role) 
VALUES (1, 'SUPER_ADMIN'), (1, 'ADMIN');
```

---
