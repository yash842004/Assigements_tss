# Final Banking App - Complete API Testing Guide

## 📋 Quick Setup Instructions

### 1. Import Postman Collection
1. Open Postman
2. Click "Import" button
3. Select `Complete_Postman_Collection.json`
4. Import `Banking_App_Environment.json` as environment

### 2. Environment Variables Setup
Set these variables in your Postman environment:
- `baseUrl`: `http://localhost:8080`
- `customerToken`: (auto-populated after login)
- `adminToken`: (auto-populated after admin login)
- `refreshToken`: (auto-populated after login)
- `customerId`: `1` (or dynamic from registration)
- `accountId`: `1` (or dynamic from account creation)

## 🚀 Testing Sequence

### Phase 1: Authentication & Setup
1. **Register Customer** → `POST {{baseUrl}}/api/customers/register`
2. **Customer Login** → `POST {{baseUrl}}/api/auth/customer/login`
3. **Create Account** → `POST {{baseUrl}}/api/accounts`

### Phase 2: Basic Operations
4. **Deposit Money** → `POST {{baseUrl}}/api/transactions/deposit`
5. **Check Balance** → `GET {{baseUrl}}/api/accounts/{{accountId}}/balance`
6. **Withdraw Money** → `POST {{baseUrl}}/api/transactions/withdraw`
7. **View Transaction History** → `GET {{baseUrl}}/api/transactions/account/{{accountId}}`

### Phase 3: Advanced Features
8. **Transfer Money** → `POST {{baseUrl}}/api/transactions/transfer`
9. **Admin Login** → `POST {{baseUrl}}/api/auth/admin/login`
10. **Generate Reports** → `GET {{baseUrl}}/api/reports/financial-summary`

## 📁 Complete API Endpoints by Module

### 🔐 Authentication Module (4 endpoints)
```
POST   {{baseUrl}}/api/auth/customer/login
POST   {{baseUrl}}/api/auth/admin/login
POST   {{baseUrl}}/api/auth/refresh
POST   {{baseUrl}}/api/auth/logout
```

### 👤 Customer Management Module (7 endpoints)
```
POST   {{baseUrl}}/api/customers/register
GET    {{baseUrl}}/api/customers/{{customerId}}
PUT    {{baseUrl}}/api/customers/{{customerId}}
PUT    {{baseUrl}}/api/customers/{{customerId}}/change-password
GET    {{baseUrl}}/api/customers?page=0&size=10&sort=id,asc
PUT    {{baseUrl}}/api/customers/{{customerId}}/activate
PUT    {{baseUrl}}/api/customers/{{customerId}}/deactivate
```

### 🏦 Account Management Module (8 endpoints)
```
POST   {{baseUrl}}/api/accounts
GET    {{baseUrl}}/api/accounts/{{accountId}}
GET    {{baseUrl}}/api/accounts/customer/{{customerId}}
GET    {{baseUrl}}/api/accounts/{{accountId}}/balance
GET    {{baseUrl}}/api/accounts/{{accountId}}/summary
PUT    {{baseUrl}}/api/accounts/{{accountId}}/close
PUT    {{baseUrl}}/api/accounts/{{accountId}}/reopen
GET    {{baseUrl}}/api/accounts?page=0&size=10&sort=id,asc
```

### 💳 Transaction Management Module (7 endpoints)
```
POST   {{baseUrl}}/api/transactions/deposit?accountId={{accountId}}&amount=500.00&description=Salary
POST   {{baseUrl}}/api/transactions/withdraw?accountId={{accountId}}&amount=100.00&description=ATM
POST   {{baseUrl}}/api/transactions/transfer
POST   {{baseUrl}}/api/transactions
GET    {{baseUrl}}/api/transactions/1
GET    {{baseUrl}}/api/transactions/account/{{accountId}}?page=0&size=10
GET    {{baseUrl}}/api/transactions/customer/{{customerId}}?page=0&size=10
```

### 👨‍💼 Admin Management Module (7 endpoints)
```
POST   {{baseUrl}}/api/admin
GET    {{baseUrl}}/api/admin/1
GET    {{baseUrl}}/api/admin?page=0&size=10&sort=id,asc
PUT    {{baseUrl}}/api/admin/1/roles/add/TRANSACTION_MANAGER
PUT    {{baseUrl}}/api/admin/1/roles/remove/CUSTOMER_MANAGER
PUT    {{baseUrl}}/api/admin/1/activate
PUT    {{baseUrl}}/api/admin/1/deactivate
```

### 📊 Reports & Analytics Module (8 endpoints)
```
GET    {{baseUrl}}/api/reports/customers?fromDate=2024-01-01&toDate=2024-12-31
GET    {{baseUrl}}/api/reports/accounts?fromDate=2024-01-01&toDate=2024-12-31
GET    {{baseUrl}}/api/reports/transactions?fromDate=2024-01-01&toDate=2024-12-31
GET    {{baseUrl}}/api/reports/financial-summary?fromDate=2024-01-01&toDate=2024-12-31
GET    {{baseUrl}}/api/reports/transaction-volume?fromDate=2024-01-01&toDate=2024-12-31
GET    {{baseUrl}}/api/reports/top-customers?limit=10
GET    {{baseUrl}}/api/reports/active-accounts?limit=10
GET    {{baseUrl}}/api/reports/largest-transactions?limit=10
```

## 🔧 Request Body Examples

### Customer Registration
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "1234567890",
  "address": "123 Main St, City, State, 12345",
  "dateOfBirth": "1990-01-15",
  "password": "securePassword123"
}
```

### Account Creation
```json
{
  "customerId": 1,
  "accountType": "SAVINGS",
  "initialDeposit": 1000.00
}
```

### Money Transfer
```json
{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 250.00,
  "description": "Transfer to friend"
}
```

### Admin Creation
```json
{
  "firstName": "Admin",
  "lastName": "User",
  "email": "admin.user@bank.com",
  "password": "adminPassword123",
  "roles": ["CUSTOMER_MANAGER", "ACCOUNT_MANAGER"]
}
```

## 🎯 Authentication Headers

### For Customer Endpoints
```
Authorization: Bearer {{customerToken}}
Content-Type: application/json
```

### For Admin Endpoints
```
Authorization: Bearer {{adminToken}}
Content-Type: application/json
```

### For Refresh Token
```
Refresh-Token: {{refreshToken}}
```

## 📊 Response Format
All APIs return standardized responses:

### Success Response
```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    // Response data here
  },
  "timestamp": "2024-01-01T10:00:00Z"
}
```

### Error Response
```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "timestamp": "2024-01-01T10:00:00Z"
}
```

## 🔍 Testing Checklist

### ✅ Authentication Flow
- [ ] Customer registration works
- [ ] Customer login returns valid JWT token
- [ ] Admin login works
- [ ] Token refresh functionality
- [ ] Logout invalidates token

### ✅ Account Operations
- [ ] Account creation with initial deposit
- [ ] Balance retrieval
- [ ] Account summary generation
- [ ] Account status management (close/reopen)

### ✅ Transaction Operations
- [ ] Deposit increases balance correctly
- [ ] Withdrawal decreases balance correctly
- [ ] Transfer between accounts works
- [ ] Transaction history is accurate
- [ ] Insufficient funds validation

### ✅ Admin Features
- [ ] Admin creation and role management
- [ ] Customer management (activate/deactivate)
- [ ] Report generation
- [ ] System analytics

### ✅ Error Handling
- [ ] Invalid credentials return 401
- [ ] Insufficient permissions return 403
- [ ] Invalid data returns 400 with validation errors
- [ ] Non-existent resources return 404

## 🚨 Common Issues & Solutions

### Issue: 401 Unauthorized
**Solution**: Ensure JWT token is included in Authorization header

### Issue: 403 Forbidden
**Solution**: Check if user has required permissions for the operation

### Issue: 400 Bad Request
**Solution**: Validate request body format and required fields

### Issue: 500 Internal Server Error
**Solution**: Check application logs and database connectivity

## 📈 Performance Testing
- Test with multiple concurrent users
- Verify transaction isolation during concurrent transfers
- Check response times for report generation
- Monitor memory usage during bulk operations

## 🔒 Security Testing
- Verify JWT token expiration
- Test with invalid/expired tokens
- Check SQL injection protection
- Validate input sanitization

---

**Total API Endpoints**: 41
**Organized in**: 6 functional modules
**Authentication**: JWT-based with refresh tokens
**Database**: MySQL with JPA/Hibernate
**Framework**: Spring Boot with Spring Security
