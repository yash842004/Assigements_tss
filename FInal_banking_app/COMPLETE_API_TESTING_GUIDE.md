# Banking App API Testing Guide - JWT Access Control

This guide provides step-by-step instructions for testing the banking application APIs with JWT-based access control in Postman.

## Enhanced Banking Features

**� Comprehensive Access Control System:**
- ✅ **Customer Data Isolation**: Each customer can ONLY access their own data (accounts, transactions, loans, FDs)
- ✅ **Cross-Customer Protection**: Prevents customers from viewing other customers' information
- ✅ **JWT Token Validation**: Secure token-based authentication with role verification
- ✅ **Hierarchical Permissions**: Customer < Admin < Super Admin access levels
- ✅ **Real-time Ownership Verification**: Every request validates data ownership

**�👑 Super Admin Management:**
- ✅ **Admin Creation**: Create new admin accounts with role assignment
- ✅ **Role Management**: Add/remove ADMIN and SUPER_ADMIN roles
- ✅ **Account Control**: Activate/deactivate admin accounts
- ✅ **Admin Oversight**: View all admins and their details
- ✅ **Access Control**: Hierarchical permissions with full system control

**🏦 Loan Management with Automatic Processing:**
- ✅ **Auto-Disbursement**: Loan approval automatically credits amount to account balance
- ✅ **EMI Processing**: Automatic monthly EMI deduction from account
- ✅ **Late Fee Management**: Automatic late fees for overdue payments
- ✅ **Insufficient Balance Handling**: Prevents EMI deduction and applies penalties when balance is insufficient
- ✅ **Transaction Recording**: All loan disbursements and EMI payments recorded in transaction history

**💰 Fixed Deposit Management:**
- ✅ **Automatic Fund Transfer**: FD creation debits from account, closure credits back to account
- ✅ **Balance Validation**: Prevents FD creation with insufficient account balance
- ✅ **Penalty Calculation**: Automatic penalty deduction for premature closures
- ✅ **Transaction Tracking**: Complete audit trail of FD-related transactions

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
7. [Fixed Deposit Operations](#fixed-deposit-operations)
8. [Admin Operations](#admin-operations)
9. [Super Admin Management Operations](#super-admin-management-operations)
10. [Reports & Analytics](#reports--analytics)
11. [Access Control Testing](#access-control-testing)
12. [Email Notification Testing](#email-notification-testing)

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
fdId                  | (leave empty)
fdNumber              | (leave empty)
newAdminId            | (leave empty)
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
**URL:** `{{baseURL}}/api/transactions/deposit?accountId={{accountId}}&amount=500.00&description=Initial deposit`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Note:** This endpoint uses query parameters, not JSON body.

### Step 16: Make a Withdrawal
**Method:** POST  
**URL:** `{{baseURL}}/api/transactions/withdraw?accountId={{accountId}}&amount=100.00&description=ATM withdrawal`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Note:** This endpoint uses query parameters, not JSON body.

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
    "accountId": {{accountId}},
    "loanType": "PERSONAL",
    "principalAmount": 10000.00,
    "interestRate": 8.5,
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

**Note:** ✅ **Automatic Disbursement:** When a loan is approved, the loan amount is automatically credited to the customer's account balance and a transaction record is created.

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

### Step 27A: Get Pending Customer Approvals (Admin Only)
**Method:** GET  
**URL:** `{{baseURL}}/api/admin/customers/pending`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

**Expected Result:** Returns list of customers awaiting approval

### Step 27B: Approve Customer Registration (Admin Only)
**Method:** PUT  
**URL:** `{{baseURL}}/api/admin/customers/{{customerId}}/approve`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{adminToken}}
```

**Body (JSON):**
```json
{
    "approvalNotes": "Customer documents verified and approved"
}
```

### Step 27C: Reject Customer Registration (Admin Only)
**Method:** PUT  
**URL:** `{{baseURL}}/api/admin/customers/{{customerId}}/reject`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{adminToken}}
```

**Body (JSON):**
```json
{
    "rejectionReason": "Incomplete documentation"
}
```

### Step 28: EMI Payment (Customer)
**Method:** POST  
**URL:** `{{baseURL}}/api/loans/emi-payment`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{customerToken}}
```

**Body (JSON):**
```json
{
    "loanId": {{loanId}},
    "amount": 885.65,
    "description": "Monthly EMI payment",
    "isAutomaticDeduction": false
}
```

**Note:** The EMI payment feature includes:
- ✅ **Automatic Account Debit:** Amount is automatically debited from the associated account balance
- ✅ **Insufficient Balance Check:** Throws exception if account doesn't have enough balance
- ✅ **Late Fee Application:** Automatically applies late fees for overdue payments
- ✅ **Outstanding Amount Update:** Updates remaining loan balance after payment

### Step 29: Get Overdue Loans (Admin Only)
**Method:** GET  
**URL:** `{{baseURL}}/api/loans/overdue`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

**Expected Result:** Returns list of loans with overdue payments including late fees

### Step 30: Process Automatic EMI (Admin Only)
**Method:** POST  
**URL:** `{{baseURL}}/api/loans/{{loanId}}/auto-emi`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

**Note:** This endpoint manually triggers automatic EMI deduction for a specific loan

## Super Admin Management Operations

### Step 31: Create New Admin (Super Admin Only)
**Method:** POST  
**URL:** `{{baseURL}}/api/admin`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{superAdminToken}}
```

**Body (JSON):**
```json
{
    "firstName": "John",
    "lastName": "AdminUser",
    "email": "john.admin@bank.com",
    "password": "admin123",
    "roles": ["ADMIN"]
}
```

**Post-Response Script:**
```javascript
if (pm.response.code === 200) {
    var response = pm.response.json();
    pm.environment.set("newAdminId", response.data.id);
    console.log("New Admin ID set: " + response.data.id);
}
```

**Expected Result:** ✅ Success for Super Admin, ❌ 403 for Regular Admin

### Step 32: Get All Admins (Super Admin Only)
**Method:** GET  
**URL:** `{{baseURL}}/api/admin?page=0&size=10`

**Headers:**
```
Authorization: Bearer {{superAdminToken}}
```

**Expected Result:** ✅ Success for Super Admin, ❌ 403 for Regular Admin

### Step 33: Get Admin Details
**Method:** GET  
**URL:** `{{baseURL}}/api/admin/{{newAdminId}}`

**Headers:**
```
Authorization: Bearer {{superAdminToken}}
```

### Step 34: Add Role to Admin (Super Admin Only)
**Method:** PUT  
**URL:** `{{baseURL}}/api/admin/{{newAdminId}}/roles/add/SUPER_ADMIN`

**Headers:**
```
Authorization: Bearer {{superAdminToken}}
```

**Expected Result:** ✅ Success - Admin now has SUPER_ADMIN role

### Step 35: Remove Role from Admin (Super Admin Only)
**Method:** PUT  
**URL:** `{{baseURL}}/api/admin/{{newAdminId}}/roles/remove/SUPER_ADMIN`

**Headers:**
```
Authorization: Bearer {{superAdminToken}}
```

**Expected Result:** ✅ Success - SUPER_ADMIN role removed

### Step 36: Deactivate Admin (Super Admin Only)
**Method:** PUT  
**URL:** `{{baseURL}}/api/admin/{{newAdminId}}/deactivate`

**Headers:**
```
Authorization: Bearer {{superAdminToken}}
```

**Expected Result:** ✅ Success - Admin account is deactivated

### Step 37: Activate Admin (Super Admin Only)
**Method:** PUT  
**URL:** `{{baseURL}}/api/admin/{{newAdminId}}/activate`

**Headers:**
```
Authorization: Bearer {{superAdminToken}}
```

**Expected Result:** ✅ Success - Admin account is reactivated

### Step 38: Test Access Control - Regular Admin Cannot Manage Admins
**Method:** GET  
**URL:** `{{baseURL}}/api/admin`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

**Expected Result:** ❌ 403 Forbidden - Regular admins cannot view all admins

### Fixed Deposit Admin Operations

#### Step 26A: Get All FDs (Admin Only)
**Method:** GET  
**URL:** `{{baseURL}}/api/fixed-deposits/all?page=0&size=20&sortBy=createdAt&sortDirection=desc`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

**Expected Result:** ✅ Success for Admin, ❌ 403 for Customer

#### Step 26B: Get FDs by Status (Admin Only)
**Method:** GET  
**URL:** `{{baseURL}}/api/fixed-deposits/status/ACTIVE?page=0&size=10`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

**Valid statuses:** ACTIVE, MATURED, CLOSED, PREMATURE_CLOSED, RENEWED

#### Step 26C: Get Matured FDs (Admin Only)
**Method:** GET  
**URL:** `{{baseURL}}/api/fixed-deposits/matured`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

#### Step 26D: Get FDs Maturing in Days (Admin Only)
**Method:** GET  
**URL:** `{{baseURL}}/api/fixed-deposits/maturing-in-days?days=30`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

**Expected Result:** ✅ Returns FDs that will mature in the next 30 days

#### Step 26E: Get FD Statistics (Admin Only)
**Method:** GET  
**URL:** `{{baseURL}}/api/fixed-deposits/statistics`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

**Expected Result:** ✅ Returns total active FD amount and count

## Reports & Analytics

### Standard Reports

#### Step 28: Get Customer Report (Admin Only)
**Method:** GET  
**URL:** `{{baseURL}}/api/reports/customers?fromDate=2025-01-01&toDate=2025-12-31`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

**Expected Result:** ✅ Success for Admin, ❌ 403 for Customer

#### Step 29: Get Account Report (Admin Only)
**Method:** GET  
**URL:** `{{baseURL}}/api/reports/accounts?fromDate=2025-01-01&toDate=2025-12-31`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

#### Step 30: Get Transaction Report (Admin Only)
**Method:** GET  
**URL:** `{{baseURL}}/api/reports/transactions?fromDate=2025-01-01&toDate=2025-12-31`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

#### Step 31: Get Financial Summary (Admin Only)
**Method:** GET  
**URL:** `{{baseURL}}/api/reports/financial-summary?fromDate=2025-01-01&toDate=2025-12-31`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

### Analytics Reports

#### Step 32: Get Daily Transaction Volume (Admin Only)
**Method:** GET  
**URL:** `{{baseURL}}/api/reports/transaction-volume?fromDate=2025-09-01&toDate=2025-09-30`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

#### Step 33: Get Top Customers by Balance (Admin Only)
**Method:** GET  
**URL:** `{{baseURL}}/api/reports/top-customers?limit=10`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

#### Step 34: Get Most Active Accounts (Admin Only)
**Method:** GET  
**URL:** `{{baseURL}}/api/reports/active-accounts?limit=10`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

#### Step 35: Get Largest Transactions (Admin Only)
**Method:** GET  
**URL:** `{{baseURL}}/api/reports/largest-transactions?limit=10`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

## Access Control Testing

### 🔐 **Customer Data Isolation Tests**

These tests verify that customers can ONLY access their own data and cannot access other customers' information.

### Test 1: Customer Accessing Own Data (Should Work)
**Method:** GET  
**URL:** `{{baseURL}}/api/customers/{{customerId}}`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Expected Result:** ✅ 200 Success - Customer can access their own profile

### Test 2: Customer Accessing Another Customer's Data (Should Fail)
**Method:** GET  
**URL:** `{{baseURL}}/api/customers/999`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Expected Result:** ❌ 403 Forbidden - "Access denied: You can only access your own data"

### Test 3: Customer Accessing Own Account (Should Work)
**Method:** GET  
**URL:** `{{baseURL}}/api/accounts/{{accountId}}`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Expected Result:** ✅ 200 Success - Customer can view their own account

### Test 4: Customer Accessing Another Customer's Account (Should Fail)
**Method:** GET  
**URL:** `{{baseURL}}/api/accounts/999`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Expected Result:** ❌ 403 Forbidden - "Access denied: You can only access your own accounts"

### Test 5: Customer Accessing Own Transactions (Should Work)
**Method:** GET  
**URL:** `{{baseURL}}/api/accounts/{{accountId}}/transactions`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Expected Result:** ✅ 200 Success - Customer can view their own transaction history

### Test 6: Customer Accessing Another Customer's Transactions (Should Fail)
**Method:** GET  
**URL:** `{{baseURL}}/api/accounts/999/transactions`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Expected Result:** ❌ 403 Forbidden - "Access denied: You can only access your own transactions"

### Test 7: Customer Accessing Own Loans (Should Work)
**Method:** GET  
**URL:** `{{baseURL}}/api/loans/customer/{{customerId}}`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Expected Result:** ✅ 200 Success - Customer can view their own loans

### Test 8: Customer Accessing Another Customer's Loans (Should Fail)
**Method:** GET  
**URL:** `{{baseURL}}/api/loans/customer/999`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Expected Result:** ❌ 403 Forbidden - "Access denied: You can only access your own loans"

### Test 9: Customer Accessing Admin Endpoints (Should Fail)
**Method:** GET  
**URL:** `{{baseURL}}/api/customers`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Expected Result:** ❌ 403 Forbidden - "Access denied: Admin privileges required"

### Test 10: Customer Trying to Approve Loans (Should Fail)
**Method:** PUT  
**URL:** `{{baseURL}}/api/loans/{{loanId}}/approve`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Expected Result:** ❌ 403 Forbidden - "Access denied: Admin privileges required"

### 👨‍💼 **Admin Access Control Tests**

### Test 11: Regular Admin vs Super Admin Operations
**Try with Regular Admin Token:**
**Method:** GET  
**URL:** `{{baseURL}}/api/admin`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

**Expected Result:** ❌ 403 Forbidden - "Access denied: Super Admin privileges required"

**Try with Super Admin Token:**
**Method:** GET  
**URL:** `{{baseURL}}/api/admin`

**Headers:**
```
Authorization: Bearer {{superAdminToken}}
```

**Expected Result:** ✅ 200 Success - Super Admin can view all admins

### Test 12: Admin Accessing Customer Data (Should Work)
**Method:** GET  
**URL:** `{{baseURL}}/api/customers/{{customerId}}`

**Headers:**
```
Authorization: Bearer {{adminToken}}
```

**Expected Result:** ✅ 200 Success - Admins can access all customer data

### 🛡️ **Token Validation Tests**

### Test 13: Invalid Token
**Method:** GET  
**URL:** `{{baseURL}}/api/customers/{{customerId}}`

**Headers:**
```
Authorization: Bearer invalid_token_here
```

**Expected Result:** ❌ 401 Unauthorized

### Test 14: Expired Token
**Method:** GET  
**URL:** `{{baseURL}}/api/customers/{{customerId}}`

**Headers:**
```
Authorization: Bearer {{expiredToken}}
```

**Expected Result:** ❌ 401 Unauthorized

### Test 15: No Authorization Header
**Method:** GET  
**URL:** `{{baseURL}}/api/customers/{{customerId}}`

**Headers:**
```
Content-Type: application/json
```

**Expected Result:** ❌ 401 Unauthorized

## 🔒 **Access Control Summary**

### **Customer Access Rules:**
- ✅ **Own Data Only**: Customers can access only their own profiles, accounts, transactions, loans, and FDs
- ❌ **No Cross-Customer Access**: Cannot access other customers' data
- ❌ **No Admin Operations**: Cannot perform administrative functions
- ❌ **No System-Wide Queries**: Cannot view lists of all customers, accounts, etc.

### **Admin Access Rules:**
- ✅ **All Customer Data**: Can access any customer's information
- ✅ **System Operations**: Can perform loan approvals, customer management, etc.
- ❌ **Role Limitations**: Regular admins cannot perform super admin operations

### **Super Admin Access Rules:**
- ✅ **Unrestricted Access**: Can perform all operations in the system
- ✅ **Admin Management**: Can create, modify, and delete other admin accounts
- ✅ **System Configuration**: Full control over all system settings

### **Security Features Implemented:**

1. **🔐 JWT Token-Based Authentication**
   - Secure token generation and validation
   - User type identification (CUSTOMER/ADMIN)
   - Role-based authorization (ADMIN/SUPER_ADMIN)

2. **🛡️ Request-Level Access Control**
   - Every sensitive endpoint validates user permissions
   - Automatic owner verification for customer data
   - Hierarchical access control (Customer < Admin < Super Admin)

3. **🔍 Data Ownership Validation**
   - Real-time verification of data ownership
   - Prevention of cross-customer data access
   - Account-transaction relationship validation

4. **⚠️ Security Exception Handling**
   - Proper HTTP status codes (401, 403)
   - Descriptive error messages for debugging
   - No sensitive information leakage in errors

5. **🔄 Multi-Layer Protection**
   - Controller-level access validation
   - Service-level business rule enforcement
   - Database-level relationship constraints

## Error Handling Examples

### Common Security Responses:

**401 Unauthorized:**
```json
{
    "success": false,
    "message": "JWT token is required",
    "data": null,
    "timestamp": "2024-12-25T10:30:00Z"
}
```

**403 Forbidden:**
```json
{
    "success": false,
    "message": "Access denied: You can only access your own data",
    "data": null,
    "timestamp": "2024-12-25T10:30:00Z"
}
```

**403 Admin Required:**
```json
{
    "success": false,
    "message": "Access denied: Admin privileges required",
    "data": null,
    "timestamp": "2024-12-25T10:30:00Z"
}
```

**403 Super Admin Required:**
```json
{
    "success": false,
    "message": "Access denied: Super Admin privileges required",
    "data": null,
    "timestamp": "2024-12-25T10:30:00Z"
}
```
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
- ❌ Cannot approve/reject loans or customers

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

7. **Loan Application Failed**
   - **Issue**: 500 Internal Server Error when applying for loan
   - **Solution**: Ensure request body includes all required fields:
     - `accountId` (not customerId)
     - `loanType` (e.g., "PERSONAL", "HOME", "AUTO")
     - `principalAmount` (minimum $1,000)
     - `interestRate` (between 0.01 and 30)
     - `termMonths` (between 6 and 360 months)
     - `purpose` (optional)

6. **Transaction Operations Not Working**
   - **Issue**: Deposits/Withdrawals not showing in transaction history
   - **Solution**: Ensure you're using query parameters, not JSON body for deposit/withdraw endpoints
   - **Correct Format**: 
     - Deposit: `/api/transactions/deposit?accountId=1&amount=100.00&description=test`
     - Withdraw: `/api/transactions/withdraw?accountId=1&amount=100.00&description=test`

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

# Fixed Deposit Operations

Fixed Deposits (FD) allow customers to invest money for a fixed period at a predetermined interest rate.

**Key Features:**
- ✅ **Automatic Fund Management**: FD creation automatically debits from specified account, FD closure automatically credits to specified account
- ✅ **Insufficient Balance Protection**: Prevents FD creation if account has insufficient funds
- ✅ **Transaction Recording**: All debits/credits are recorded in transaction history
- ✅ **Penalty Calculation**: Automatic penalty deduction for premature closures

### Prerequisites
- Customer must be authenticated
- Customer should have sufficient funds in their account for FD creation

### Step 22A: Calculate FD Maturity Amount
**Method:** GET  
**URL:** `{{baseURL}}/api/fixed-deposits/calculate-maturity?principalAmount=100000&interestRate=7.5&tenureMonths=12`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Expected Result:** ✅ Returns calculated maturity amount

### Step 22B: Create Fixed Deposit
**Method:** POST  
**URL:** `{{baseURL}}/api/fixed-deposits/create`

**Note:** ⚠️ **Automatic Account Debit**: When creating an FD, the principal amount will be automatically debited from the specified account. Ensure the account has sufficient balance, or you'll get an "Insufficient Funds" error.

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
    "principalAmount": 100000.00,
    "interestRate": 7.5,
    "tenureMonths": 12,
    "autoRenewal": false,
    "prematureWithdrawalAllowed": true,
    "nomineeName": "John Doe Jr",
    "nomineeRelationship": "Son"
}
```

**Test Script:**
```javascript
if (pm.response.code === 201) {
    var response = pm.response.json();
    pm.environment.set("fdId", response.data.id);
    pm.environment.set("fdNumber", response.data.fdNumber);
    console.log("FD ID set: " + response.data.id);
    console.log("FD Number set: " + response.data.fdNumber);
}
```

### Step 22C: Get FD Details by ID
**Method:** GET  
**URL:** `{{baseURL}}/api/fixed-deposits/{{fdId}}`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Expected Result:** ✅ Returns FD details with interest earned and maturity information

### Step 22D: Get FD Details by FD Number
**Method:** GET  
**URL:** `{{baseURL}}/api/fixed-deposits/fd-number/{{fdNumber}}`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

### Step 22E: Get Customer's All FDs
**Method:** GET  
**URL:** `{{baseURL}}/api/fixed-deposits/customer/{{customerId}}?page=0&size=10&sortBy=createdAt&sortDirection=desc`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

### Step 22F: Calculate Interest Earned on FD
**Method:** GET  
**URL:** `{{baseURL}}/api/fixed-deposits/{{fdId}}/interest-earned`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Expected Result:** ✅ Returns current interest earned amount

### Step 22G: Close/Withdraw FD (Premature)
**Method:** PUT  
**URL:** `{{baseURL}}/api/fixed-deposits/close`

**Note:** ⚠️ **Automatic Account Credit**: When closing an FD, the maturity/closure amount (minus penalty, if applicable) will be automatically credited to the specified account.

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{customerToken}}
```

**Body (JSON):**
```json
{
    "fdId": {{fdId}},
    "accountId": {{accountId}},
    "reason": "Emergency fund needed",
    "isPremature": true
}
```

**Expected Result:** ✅ FD closed with penalty applied (if applicable)

### Step 22H: Renew Matured FD
**Method:** PUT  
**URL:** `{{baseURL}}/api/fixed-deposits/{{fdId}}/renew`

**Headers:**
```
Authorization: Bearer {{customerToken}}
```

**Note:** This will only work if the FD has matured

---

## 23. Email Notification Testing 📧

**Important:** All email notifications are sent automatically when performing credit/debit operations. Monitor your email or check application logs to verify notifications are sent.

### 23A: Test Transaction Email Notifications

**Perform these operations to test email notifications:**

1. **Deposit Operation** (Credit Notification)
   - Make a deposit using Step 8A
   - ✅ **Expected Email**: Balance update with credit details

2. **Withdrawal Operation** (Debit Notification)
   - Make a withdrawal using Step 8B
   - ✅ **Expected Email**: Balance update with debit details

3. **Transfer Operation** (Dual Notifications)
   - Perform a transfer using Step 9A
   - ✅ **Expected Emails**: 
     - Debit notification to sender
     - Credit notification to receiver

### 23B: Test Loan Email Notifications

1. **Loan Approval Notification**
   - Approve a loan using Step 17A
   - ✅ **Expected Email**: Loan disbursement notification with account balance update

2. **EMI Deduction Notification**
   - Make EMI payment using Step 18A
   - ✅ **Expected Email**: EMI payment confirmation with remaining balance

3. **Automatic EMI Notification**
   - Triggered by scheduled task (automatic)
   - ✅ **Expected Email**: Automatic EMI deduction confirmation

### 23C: Test Fixed Deposit Email Notifications

1. **FD Creation Notification**
   - Create an FD using Step 22A
   - ✅ **Expected Emails**:
     - FD creation confirmation
     - Account debit notification for FD amount

2. **FD Maturity Notification**
   - Triggered by scheduled task when FD matures
   - ✅ **Expected Email**: FD maturity notification with maturity amount

3. **FD Closure Notification**
   - Close an FD using Step 22G
   - ✅ **Expected Emails**:
     - FD closure confirmation
     - Account credit notification for closure amount

### 23D: Email Notification Features

**All Email Notifications Include:**
- ✅ Customer name and email
- ✅ Account number and current balance
- ✅ Transaction amount and type (CREDIT/DEBIT)
- ✅ Previous balance and new balance
- ✅ Transaction reference ID
- ✅ Timestamp of operation
- ✅ Relevant operation details (loan number, FD details, etc.)

**Email Configuration:**
- Configure SMTP settings in `application.properties`
- Email notifications are sent asynchronously
- Failed email attempts are logged for troubleshooting

### 23E: Monitoring Email Notifications

**In Application Logs:**
```
[INFO] Email notification sent successfully to: customer@example.com
[INFO] Transaction notification sent for account: 1234567890
[INFO] Balance update notification sent for CREDIT operation
```

**Check Your Email Client:**
- Look for emails from the banking application
- Verify all required information is included
- Confirm email formatting and content accuracy

**Troubleshooting:**
- Check SMTP configuration in `application.properties`
- Verify email service implementation
- Check application logs for email sending errors
- Ensure recipient email addresses are valid
