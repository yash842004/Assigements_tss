# Banking App API Testing Guide - JWT Access Control

This guide provides step-by-step instructions for testing the banking application APIs with JWT-based access control in Postman.

## JWT Algorithm Information

**Algorithm Used**: **HS256 (HMAC-SHA256)**
- **Type**: Symmetric key cryptographic algorithm
- **Key Type**: HMAC (Hash-based Message Authentication Code) with SHA-256
- **Security**: Uses a shared secret key to both sign and verify tokens
- **Key Length**: Minimum 32 bytes (256 bits) as enforced by the application
- **Token Structure**: Header.Payload.Signature (standard JWT format)

## Default Admin Credentials

**Super Admin:**
- Email: `admin@bank.com`
- Password: `admin123`
- Roles: SUPER_ADMIN, ADMIN

**Regular Admin:**
- Email: `user@bank.com`
- Password: `user123`
- Roles: ADMIN

## Table of Contents
1. [Environment Setup](#environment-setup)
2. [Authentication](#authentication)
3. [Customer Operations](#customer-operations)
4. [Account Operations](#account-operations)
5. [Transaction Operations](#transaction-operations)
6. [Loan Operations](#loan-operations)
7. [Admin Operations](#admin-operations)
8. [Access Control Testing](#access-control-testing)

## Environment Setup

### 1. Create Environment Variables in Postman
```
Variable Name          | Initial Value
--------------------- | ----------------
baseURL               | http://localhost:8080
customerToken         | (leave empty)
adminToken            | (leave empty)
superAdminToken       | (leave empty)
customerId            | (leave empty)
accountId             | (leave empty)
loanId                | (leave empty)
transactionId         | (leave empty)
```

### 2. Start the Application
```bash
mvn spring-boot:run
```
Application will start on: `http://localhost:8080`

## Authentication

### Step 1: Admin Login (Super Admin)
**Method:** POST  
**URL:** `{{baseURL}}/api/auth/admin/login`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
    "email": "admin@bank.com",
    "password": "admin123"
}
```

**Post-Response Script:**
```javascript
if (pm.response.code === 200) {
    var response = pm.response.json();
    pm.environment.set("superAdminToken", response.data.token);
    console.log("Super Admin Token set successfully");
}
```

### Step 2: Admin Login (Regular Admin)
**Method:** POST  
**URL:** `{{baseURL}}/api/auth/admin/login`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
    "email": "user@bank.com",
    "password": "user123"
}
```

**Post-Response Script:**
```javascript
if (pm.response.code === 200) {
    var response = pm.response.json();
    pm.environment.set("adminToken", response.data.token);
    console.log("Admin Token set successfully");
}
```

### Step 3: Customer Registration
**Method:** POST  
**URL:** `{{baseURL}}/api/auth/customer/register`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
    "firstName": "John",
    "lastName": "Doe",
    "fullName": "John Doe",
    "email": "john.doe@email.com",
    "password": "Customer123!",
    "phone": "+1234567890",
    "address": "123 Main St, New York, NY, 10001, USA",
    "dateOfBirth": "1990-01-15"
}
```

**Post-Response Script:**
```javascript
if (pm.response.code === 200) {
    var response = pm.response.json();
    pm.environment.set("customerId", response.data.id);
    console.log("Customer ID set: " + response.data.id);
}
```

### Step 4: Approve Customer (Super Admin Only)
**Method:** PUT  
**URL:** `{{baseURL}}/api/admin/customers/{{customerId}}/approve`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{superAdminToken}}
```

**Body (JSON):**
```json
{
    "approvalNotes": "Customer verification completed successfully"
}
```

### Step 5: Customer Login
**Method:** POST  
**URL:** `{{baseURL}}/api/auth/customer/login`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
    "email": "john.doe@email.com",
    "password": "Customer123!"
}
```

**Post-Response Script:**
```javascript
if (pm.response.code === 200) {
    var response = pm.response.json();
    pm.environment.set("customerToken", response.data.token);
    console.log("Customer Token set successfully");
}
```

## Customer Operations

### Step 6: Get Customer Profile (Own Data)
**Method:** GET  
**URL:** `{{baseURL}}/api/customers/{{customerId}}`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Expected Result:** ✅ Success - Customer can access own data

### Step 7: Try to Access Another Customer's Data (Should Fail)
**Method:** GET  
**URL:** `{{baseURL}}/api/customers/999`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Expected Result:** ❌ 403 Forbidden - Access denied

### Step 8: Update Customer Profile
**Method:** PUT  
**URL:** `{{baseURL}}/api/customers/{{customerId}}`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{customerToken}}
```

**Body (JSON):**
```json
{
    "fullName": "John Updated Doe",
    "email": "john.doe@email.com",
    "phone": "9876543210",
    "address": "456 Updated St, New York, NY, 10001, USA"
}
```

**Expected Result:** ✅ Success - Customer can update own data

### Step 9: Change Password
**Method:** PUT  
**URL:** `{{baseURL}}/api/customers/{{customerId}}/change-password`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{customerToken}}
```

**Body (JSON):**
```json
{
    "currentPassword": "Customer123!",
    "newPassword": "NewPassword123!",
    "confirmPassword": "NewPassword123!"
}
```

## Account Operations

### Step 10: Create Account
**Method:** POST  
**URL:** `{{baseURL}}/api/accounts`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{customerToken}}
```

**Body (JSON):**
```json
{
    "customerId": {{customerId}},
    "accountType": "SAVINGS",
    "initialDeposit": 1000.00
}
```

**Post-Response Script:**
```javascript
if (pm.response.code === 200) {
    var response = pm.response.json();
    pm.environment.set("accountId", response.data.id);
    console.log("Account ID set: " + response.data.id);
}
```

### Step 11: Get Account Details (Own Account)
**Method:** GET  
**URL:** `{{baseURL}}/api/accounts/{{accountId}}`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Expected Result:** ✅ Success - Customer can access own account

### Step 12: Get Account Balance
**Method:** GET  
**URL:** `{{baseURL}}/api/accounts/{{accountId}}/balance`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

### Step 13: Get Accounts by Customer ID
**Method:** GET  
**URL:** `{{baseURL}}/api/accounts/customer/{{customerId}}`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

### Step 14: Get Account Summary
**Method:** GET  
**URL:** `{{baseURL}}/api/accounts/{{accountId}}/summary`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

## Transaction Operations

### Step 15: Make a Deposit
**Method:** POST  
**URL:** `{{baseURL}}/api/transactions/deposit`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{customerToken}}
```

**Body (JSON):**
```json
{
    "accountId": {{accountId}},
    "amount": 500.00,
    "description": "Initial deposit"
}
```

### Step 16: Make a Withdrawal
**Method:** POST  
**URL:** `{{baseURL}}/api/transactions/withdraw`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{customerToken}}
```

**Body (JSON):**
```json
{
    "accountId": {{accountId}},
    "amount": 100.00,
    "description": "ATM withdrawal"
}
```

### Step 17: Transfer Money
**Method:** POST  
**URL:** `{{baseURL}}/api/transactions/transfer`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{customerToken}}
```

**Body (JSON):**
```json
{
    "fromAccountId": {{accountId}},
    "toAccountNumber": "ACC1234567890",
    "amount": 250.00,
    "description": "Transfer to friend"
}
```

### Step 18: Get Transaction History
**Method:** GET  
**URL:** `{{baseURL}}/api/transactions/account/{{accountId}}`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

## Loan Operations

### Step 19: Apply for Loan
**Method:** POST  
**URL:** `{{baseURL}}/api/loans/apply`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{customerToken}}
```

**Body (JSON):**
```json
{
    "customerId": {{customerId}},
    "accountId": {{accountId}},
    "loanType": "PERSONAL",
    "principalAmount": 10000.00,
    "termMonths": 24,
    "purpose": "Home renovation"
}
```

**Post-Response Script:**
```javascript
if (pm.response.code === 200) {
    var response = pm.response.json();
    pm.environment.set("loanId", response.data.id);
    console.log("Loan ID set: " + response.data.id);
}
```

### Step 20: Get Loan Details (Own Loan)
**Method:** GET  
**URL:** `{{baseURL}}/api/loans/{{loanId}}`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

### Step 21: Get Customer Loans
**Method:** GET  
**URL:** `{{baseURL}}/api/loans/customer/{{customerId}}`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

### Step 22: Try to Access Another Customer's Loans (Should Fail)
**Method:** GET  
**URL:** `{{baseURL}}/api/loans/customer/999`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Expected Result:** ❌ 403 Forbidden - Access denied

## Admin Operations

### Step 23: Approve Loan (Admin)
**Method:** PUT  
**URL:** `{{baseURL}}/api/loans/{{loanId}}/approve`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{adminToken}}
```

**Body (JSON):**
```json
{
    "approvalNotes": "Loan approved after verification",
    "approvedAmount": 10000.00
}
```

### Step 24: Get All Customers (Admin Only)
**Method:** GET  
**URL:** `{{baseURL}}/api/customers`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

**Expected Result:** ✅ Success for Admin, ❌ 403 for Customer

### Step 25: Get All Accounts (Admin Only)
**Method:** GET  
**URL:** `{{baseURL}}/api/accounts`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

### Step 26: Get All Loans (Admin Only)
**Method:** GET  
**URL:** `{{baseURL}}/api/loans`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

### Step 27: Get Pending Loan Applications (Admin Only)
**Method:** GET  
**URL:** `{{baseURL}}/api/loans/pending`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

## Access Control Testing

### Test 1: Customer Accessing Another Customer's Data
**Method:** GET  
**URL:** `{{baseURL}}/api/customers/999`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Expected Result:** ❌ 403 Forbidden

### Test 2: Customer Accessing Admin Endpoints
**Method:** GET  
**URL:** `{{baseURL}}/api/customers`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Expected Result:** ❌ 403 Forbidden

### Test 3: Regular Admin vs Super Admin Operations
**Try with Regular Admin Token:**
**Method:** PUT  
**URL:** `{{baseURL}}/api/admin/customers/{{customerId}}/approve`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

**Expected Result:** ❌ 403 Forbidden (Only Super Admin can approve customers)

**Try with Super Admin Token:**
**Method:** PUT  
**URL:** `{{baseURL}}/api/admin/customers/{{customerId}}/approve`

**Headers:**
```
Authorization: Bearer {{superAdminToken}}
```

**Expected Result:** ✅ Success

### Test 4: Customer Accessing Another Account
**Method:** GET  
**URL:** `{{baseURL}}/api/accounts/999`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Expected Result:** ❌ 403 Forbidden

### Test 5: Invalid Token
**Method:** GET  
**URL:** `{{baseURL}}/api/customers/{{customerId}}`

**Headers:**
```
Authorization: Bearer invalid_token_here
```

**Expected Result:** ❌ 401 Unauthorized

## Response Status Codes

- **200 OK**: Request successful
- **201 Created**: Resource created successfully
- **400 Bad Request**: Invalid request data
- **401 Unauthorized**: Invalid or missing authentication token
- **403 Forbidden**: Access denied due to insufficient permissions
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server error

## Access Control Rules Summary

### Customer Access Rules:
- ✅ Can access their own data (profile, accounts, transactions, loans)
- ❌ Cannot access other customers' data
- ❌ Cannot access admin endpoints
- ❌ Cannot approve loans or customers

### Regular Admin Access Rules:
- ✅ Can view all customers, accounts, transactions, loans
- ✅ Can approve/reject loans
- ✅ Can view reports
- ❌ Cannot approve/reject customers (Super Admin only)
- ❌ Cannot create/delete other admins (Super Admin only)

### Super Admin Access Rules:
- ✅ Full access to all operations
- ✅ Can approve/reject customers
- ✅ Can create/delete admins
- ✅ Can perform all regular admin operations

## Troubleshooting

### Common Issues:

1. **401 Unauthorized**
   - Check if token is included in Authorization header
   - Verify token format: `Bearer <token>`
   - Ensure token hasn't expired

2. **403 Forbidden**
   - Verify user has correct permissions
   - Check if trying to access another user's data
   - Confirm admin role for admin operations

3. **Token Expired**
   - Re-login to get new token
   - Update environment variables with new token

4. **Customer Not Approved**
   - Use Super Admin token to approve customer
   - Customer must be approved before account operations

5. **403 Forbidden on Customer Operations (Change Password, Update Profile, etc.)**
   - **Root Cause**: Customer ID mismatch or customer not properly logged in after approval
   - **Solution Steps**:
     1. Ensure customer is approved (Step 4) before attempting login
     2. After approval, perform fresh customer login (Step 5) to get updated token
     3. Verify the `customerId` environment variable matches the ID in the URL
     4. Check that you're using the correct `customerToken` from the login response
   - **Common Issue**: Using old token or wrong customer ID in URL path

6. **Request Body Field Name Errors**
   - **Password Change**: Use `currentPassword` (not `oldPassword`)
   - **Customer Update**: Use `fullName` (not `firstName`/`lastName`), `phone` (not `phoneNumber`), `address` as string (not object)
   - **Common Error**: "Cannot invoke...because the return value...is null" indicates wrong field names

7. **Admin Login Failed - "Invalid admin credentials"**
   - **Solution**: Clear your database and restart the application
   - Default credentials will be recreated with proper password encoding
   - Use the correct credentials:
     - **Super Admin**: `admin@bank.com` / `admin123`
     - **Regular Admin**: `user@bank.com` / `user123`
   
   **Alternative**: If you want to keep existing data, update admin passwords manually:
   ```sql
   -- Connect to your database and run:
   UPDATE admins SET password_hash = '$2a$10$...' WHERE email = 'admin@bank.com';
   ```

### Environment Variable Check:
Before testing, ensure all environment variables are set:
```javascript
// Add this as a Pre-request Script to check variables
console.log("Base URL: " + pm.environment.get("baseURL"));
console.log("Customer Token: " + pm.environment.get("customerToken"));
console.log("Admin Token: " + pm.environment.get("adminToken"));
console.log("Customer ID: " + pm.environment.get("customerId"));
console.log("Account ID: " + pm.environment.get("accountId"));
```

This completes the comprehensive testing guide for the Banking App with JWT-based access control!
