# Complete Banking Application API Testing Guide

## Table of Contents

1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [Environment Setup](#environment-setup)
4. [Authentication](#authentication)
5. [Admin User Guide](#admin-user-guide)
6. [Customer User Guide](#customer-user-guide)
7. [Joint Account Management](#joint-account-management)
8. [Advanced Features](#advanced-features)
9. [Testing Scenarios](#testing-scenarios)
10. [Troubleshooting](#troubleshooting)
11. [API Reference](#api-reference)

---

## Overview

This comprehensive guide provides step-by-step instructions for testing all features of the Complete Banking Application API, including:

### 🏦 Core Features
- **Customer Registration & Authentication**
- **Account Management (Savings, Current, Joint Accounts)**
- **Transaction Processing (Deposit, Withdrawal, Transfer)**
- **Fixed Deposit Management**
- **Loan Management**
- **Admin Operations**
- **Reporting & Analytics**

### ✨ Advanced Features
- **Joint Account Support** (Multi-holder accounts)
- **Email Notifications** (All transactions)
- **Role-based Access Control**
- **Real-time Balance Updates**
- **Automated FD Maturity & Renewal**
- **EMI Calculations & Payments**

---

## Prerequisites

### Required Software
- **Java 17+**
- **Maven 3.6+**
- **MySQL 8.0+** (or H2 for testing)
- **Postman** (recommended for API testing)
- **Git**

### Development Tools (Optional)
- **IntelliJ IDEA** or **Eclipse**
- **MySQL Workbench**
- **VS Code** with REST Client extension

---

## Environment Setup

### 1. Clone and Setup Application

```bash
# Clone the repository
git clone <repository-url>
cd FInal_banking_app

# Build the application
mvn clean install

# Run the application
mvn spring-boot:run
```

### 2. Database Configuration

**Option A: MySQL Database**
```properties
# application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/banking_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

**Option B: H2 In-Memory Database (for testing)**
```properties
# application-test.properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=create-drop
```

### 3. Email Configuration (Optional)

```properties
# Gmail Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### 4. Verify Application Startup

```bash
# Check if application is running
curl http://localhost:8080/actuator/health

# Expected Response
{
  "status": "UP"
}
```

---

## Authentication

### Understanding User Roles

The application supports three user types:

1. **SUPER_ADMIN** - Full system access
2. **ADMIN** - Customer and account management
3. **CUSTOMER** - Personal banking operations

### Default Credentials

**Super Admin**
- Username: `admin`
- Password: `admin123`

**Test Customers**
- Created during registration process

---

## Admin User Guide

### 🔐 Step 1: Admin Authentication

#### 1.1 Admin Login

**Request:**
```http
POST http://localhost:8080/api/auth/admin/login
Content-Type: application/json

{
    "username": "admin",
    "password": "admin123"
}
```

**Expected Response:**
```json
{
    "success": true,
    "message": "Login successful",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9...",
        "username": "admin",
        "role": "SUPER_ADMIN",
        "expiresIn": 86400
    }
}
```

**💡 Important:** Copy the token value for use in subsequent requests.

---

### 👥 Step 2: Customer Management

#### 2.1 View All Customers

**Request:**
```http
GET http://localhost:8080/api/admin/customers?page=0&size=10
Authorization: Bearer YOUR_ADMIN_TOKEN
```

**Response Structure:**
```json
{
    "success": true,
    "data": {
        "content": [
            {
                "id": 1,
                "firstName": "John",
                "lastName": "Doe",
                "email": "john.doe@example.com",
                "phoneNumber": "1234567890",
                "status": "ACTIVE",
                "registrationDate": "2024-01-15T10:30:00"
            }
        ],
        "pageable": { ... },
        "totalElements": 5
    }
}
```

#### 2.2 Get Customer Details

**Request:**
```http
GET http://localhost:8080/api/admin/customers/{customerId}
Authorization: Bearer YOUR_ADMIN_TOKEN
```

#### 2.3 Customer Status Management

**Deactivate Customer:**
```http
PUT http://localhost:8080/api/admin/customers/{customerId}/deactivate
Authorization: Bearer YOUR_ADMIN_TOKEN
```

**Activate Customer:**
```http
PUT http://localhost:8080/api/admin/customers/{customerId}/activate
Authorization: Bearer YOUR_ADMIN_TOKEN
```

#### 2.4 Approve Customer Registration

**Request:**
```http
PUT http://localhost:8080/api/admin/customers/{customerId}/approve
Authorization: Bearer YOUR_ADMIN_TOKEN
Content-Type: application/json

{
    "approvalNotes": "All documents verified successfully"
}
```

**Expected Response:**
```json
{
    "success": true,
    "message": "Customer approved successfully",
    "data": {
        "id": 3,
        "firstName": "Jane",
        "lastName": "Smith",
        "email": "jane.smith@example.com",
        "phoneNumber": "+1234567890",
        "address": "456 Oak Street",
        "status": "ACTIVE",
        "registrationDate": "2024-01-15T10:30:00",
        "lastUpdated": "2024-01-15T14:45:00"
    }
}
```

#### 2.5 Reject Customer Registration

**Request:**
```http
PUT http://localhost:8080/api/admin/customers/{customerId}/reject
Authorization: Bearer YOUR_ADMIN_TOKEN
Content-Type: application/json

{
    "rejectionReason": "Incomplete documentation provided during registration"
}
```

**Expected Response:**
```json
{
    "success": true,
    "message": "Customer rejected successfully",
    "data": {
        "id": 3,
        "firstName": "Jane",
        "lastName": "Smith",
        "email": "jane.smith@example.com",
        "phoneNumber": "+1234567890",
        "address": "456 Oak Street",
        "status": "INACTIVE",
        "registrationDate": "2024-01-15T10:30:00",
        "lastUpdated": "2024-01-15T14:45:00"
    }
}
```

**Validation Rules:**
- `rejectionReason`: Required, maximum 500 characters
- Only admins can approve/reject customers
- Customer receives email notification with approval/rejection details

#### 2.6 Get Pending Customer Registrations

**Request:**
```http
GET http://localhost:8080/api/admin/customers/pending?page=0&size=10
Authorization: Bearer YOUR_ADMIN_TOKEN
```

**Expected Response:**
```json
{
    "success": true,
    "message": "Pending customers retrieved successfully",
    "data": {
        "content": [
            {
                "id": 3,
                "firstName": "Jane",
                "lastName": "Smith",
                "email": "jane.smith@example.com",
                "phoneNumber": "+1234567890",
                "address": "456 Oak Street",
                "status": "PENDING_APPROVAL",
                "registrationDate": "2024-01-15T10:30:00"
            }
        ],
        "pageable": { ... },
        "totalElements": 2
    }
}
```

---

### 🏦 Step 3: Account Management (Admin View)

#### 3.1 View All Accounts

**Request:**
```http
GET http://localhost:8080/api/accounts?page=0&size=20
Authorization: Bearer YOUR_ADMIN_TOKEN
```

#### 3.2 Account Operations

**Close Account:**
```http
PUT http://localhost:8080/api/accounts/{accountId}/close
Authorization: Bearer YOUR_ADMIN_TOKEN
```

**Reopen Account:**
```http
PUT http://localhost:8080/api/accounts/{accountId}/reopen
Authorization: Bearer YOUR_ADMIN_TOKEN
```

---

### 💰 Step 4: Loan Management (Admin)

#### 4.1 View All Loan Applications

**Request:**
```http
GET http://localhost:8080/api/admin/loans?page=0&size=10
Authorization: Bearer YOUR_ADMIN_TOKEN
```

#### 4.2 Approve Loan Application

**Request:**
```http
PUT http://localhost:8080/api/loans/{loanId}/approve
Authorization: Bearer YOUR_ADMIN_TOKEN
```

**Expected Response:**
```json
{
    "success": true,
    "message": "Loan approved successfully",
    "data": {
        "id": 1,
        "status": "APPROVED",
        "approvedDate": "2024-01-15T14:30:00"
    }
}
```

#### 4.3 Reject Loan Application

**Request:**
```http
PUT http://localhost:8080/api/loans/{loanId}/reject
Authorization: Bearer YOUR_ADMIN_TOKEN
Content-Type: application/json

{
    "rejectionReason": "Insufficient credit score"
}
```

---

### 📊 Step 5: Reports and Analytics

#### 5.1 Account Balance Report

**Request:**
```http
GET http://localhost:8080/api/reports/account-balances?page=0&size=10
Authorization: Bearer YOUR_ADMIN_TOKEN
```

#### 5.2 Transaction Volume Report

**Request:**
```http
GET http://localhost:8080/api/reports/transaction-volume?startDate=2024-01-01&endDate=2024-01-31&page=0&size=10
Authorization: Bearer YOUR_ADMIN_TOKEN
```

#### 5.3 Customer Activity Report

**Request:**
```http
GET http://localhost:8080/api/reports/customer-activity?page=0&size=10
Authorization: Bearer YOUR_ADMIN_TOKEN
```

#### 5.4 Most Active Accounts

**Request:**
```http
GET http://localhost:8080/api/reports/most-active-accounts?page=0&size=10
Authorization: Bearer YOUR_ADMIN_TOKEN
```

---

### 👨‍💼 Step 6: Admin Management

#### 6.1 Create New Admin

**Request:**
```http
POST http://localhost:8080/api/admin/admins
Authorization: Bearer YOUR_ADMIN_TOKEN
Content-Type: application/json

{
    "username": "manager1",
    "password": "securePassword123",
    "email": "manager1@bank.com",
    "role": "ADMIN"
}
```

#### 6.2 Manage Admin Roles

**Update Admin Role:**
```http
PUT http://localhost:8080/api/admin/admins/{adminId}/role
Authorization: Bearer YOUR_ADMIN_TOKEN
Content-Type: application/json

{
    "role": "SUPER_ADMIN"
}
```

---

## Customer User Guide

### 🔐 Step 1: Customer Registration & Authentication

#### 1.1 Customer Registration

**Request:**
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phoneNumber": "1234567890",
    "address": "123 Main St, City, State 12345",
    "dateOfBirth": "1990-01-15",
    "password": "securePassword123"
}
```

**Expected Response:**
```json
{
    "success": true,
    "message": "Customer registered successfully",
    "data": {
        "id": 1,
        "firstName": "John",
        "lastName": "Doe",
        "email": "john.doe@example.com",
        "status": "ACTIVE"
    }
}
```

#### 1.2 Customer Login

**Request:**
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
    "email": "john.doe@example.com",
    "password": "securePassword123"
}
```

**Expected Response:**
```json
{
    "success": true,
    "message": "Login successful",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9...",
        "customerId": 1,
        "customerName": "John Doe",
        "email": "john.doe@example.com",
        "expiresIn": 86400
    }
}
```

---

### 👤 Step 2: Profile Management

#### 2.1 Get Customer Profile

**Request:**
```http
GET http://localhost:8080/api/customers/profile
Authorization: Bearer YOUR_CUSTOMER_TOKEN
```

#### 2.2 Update Profile

**Request:**
```http
PUT http://localhost:8080/api/customers/profile
Authorization: Bearer YOUR_CUSTOMER_TOKEN
Content-Type: application/json

{
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "1234567890",
    "address": "456 Updated Address, City, State 12345"
}
```

---

### 🏦 Step 3: Account Management

#### 3.1 Create Savings Account

**Request:**
```http
POST http://localhost:8080/api/accounts
Authorization: Bearer YOUR_CUSTOMER_TOKEN
Content-Type: application/json

{
    "customerId": 1,
    "accountType": "SAVINGS",
    "initialDeposit": 1000.00
}
```

**Expected Response:**
```json
{
    "success": true,
    "message": "Account created successfully",
    "data": {
        "id": 1,
        "accountNumber": "ACC001234567890",
        "accountType": "SAVINGS",
        "balance": 1000.00,
        "status": "ACTIVE",
        "customerId": 1,
        "customerName": "John Doe",
        "createdDate": "2024-01-15T10:30:00"
    }
}
```

#### 3.2 Create Current Account

**Request:**
```http
POST http://localhost:8080/api/accounts
Authorization: Bearer YOUR_CUSTOMER_TOKEN
Content-Type: application/json

{
    "customerId": 1,
    "accountType": "CURRENT",
    "initialDeposit": 5000.00
}
```

#### 3.3 Get Account Details

**Request:**
```http
GET http://localhost:8080/api/accounts/{accountId}
Authorization: Bearer YOUR_CUSTOMER_TOKEN
```

#### 3.4 Get Account Balance

**Request:**
```http
GET http://localhost:8080/api/accounts/{accountId}/balance
Authorization: Bearer YOUR_CUSTOMER_TOKEN
```

#### 3.5 Get All Customer Accounts

**Request:**
```http
GET http://localhost:8080/api/accounts/customer/{customerId}
Authorization: Bearer YOUR_CUSTOMER_TOKEN
```

---

### 💳 Step 4: Transaction Operations

#### 4.1 Deposit Money

**Request:**
```http
POST http://localhost:8080/api/transactions/deposit
Authorization: Bearer YOUR_CUSTOMER_TOKEN
Content-Type: application/json

{
    "accountId": 1,
    "amount": 500.00,
    "description": "Salary deposit"
}
```

**Expected Response:**
```json
{
    "success": true,
    "message": "Deposit successful",
    "data": {
        "id": 1,
        "accountId": 1,
        "transactionType": "CREDIT",
        "amount": 500.00,
        "description": "Salary deposit",
        "balance": 1500.00,
        "transactionDate": "2024-01-15T14:30:00"
    }
}
```

#### 4.2 Withdraw Money

**Request:**
```http
POST http://localhost:8080/api/transactions/withdraw
Authorization: Bearer YOUR_CUSTOMER_TOKEN
Content-Type: application/json

{
    "accountId": 1,
    "amount": 200.00,
    "description": "ATM withdrawal"
}
```

#### 4.3 Transfer Money

**Request:**
```http
POST http://localhost:8080/api/transactions/transfer
Authorization: Bearer YOUR_CUSTOMER_TOKEN
Content-Type: application/json

{
    "fromAccountId": 1,
    "toAccountId": 2,
    "amount": 300.00,
    "description": "Transfer to savings"
}
```

#### 4.4 Get Transaction History

**Request:**
```http
GET http://localhost:8080/api/transactions/account/{accountId}?page=0&size=10&sortBy=transactionDate&sortDir=desc
Authorization: Bearer YOUR_CUSTOMER_TOKEN
```

---

### 🏛️ Step 5: Fixed Deposit Management

#### 5.1 Create Fixed Deposit

**Request:**
```http
POST http://localhost:8080/api/fixed-deposits
Authorization: Bearer YOUR_CUSTOMER_TOKEN
Content-Type: application/json

{
    "accountId": 1,
    "principalAmount": 10000.00,
    "tenureMonths": 12,
    "interestRate": 7.5
}
```

**Expected Response:**
```json
{
    "success": true,
    "message": "Fixed deposit created successfully",
    "data": {
        "id": 1,
        "fdNumber": "FD001234567890",
        "accountId": 1,
        "principalAmount": 10000.00,
        "interestRate": 7.5,
        "tenureMonths": 12,
        "maturityAmount": 10750.00,
        "startDate": "2024-01-15",
        "maturityDate": "2025-01-15",
        "status": "ACTIVE"
    }
}
```

#### 5.2 Get FD Details

**Request:**
```http
GET http://localhost:8080/api/fixed-deposits/{fdId}
Authorization: Bearer YOUR_CUSTOMER_TOKEN
```

#### 5.3 Get Customer FDs

**Request:**
```http
GET http://localhost:8080/api/fixed-deposits/customer/{customerId}
Authorization: Bearer YOUR_CUSTOMER_TOKEN
```

#### 5.4 Close FD (Premature)

**Request:**
```http
PUT http://localhost:8080/api/fixed-deposits/{fdId}/close
Authorization: Bearer YOUR_CUSTOMER_TOKEN
```

#### 5.5 Enable Auto-Renewal

**Request:**
```http
PUT http://localhost:8080/api/fixed-deposits/{fdId}/auto-renewal
Authorization: Bearer YOUR_CUSTOMER_TOKEN
Content-Type: application/json

{
    "autoRenewal": true
}
```

---

### 💰 Step 6: Loan Management

#### 6.1 Apply for Personal Loan

**Request:**
```http
POST http://localhost:8080/api/loans
Authorization: Bearer YOUR_CUSTOMER_TOKEN
Content-Type: application/json

{
    "customerId": 1,
    "loanType": "PERSONAL",
    "principalAmount": 50000.00,
    "interestRate": 12.5,
    "tenureMonths": 24,
    "purpose": "Home renovation"
}
```

#### 6.2 Apply for Home Loan

**Request:**
```http
POST http://localhost:8080/api/loans
Authorization: Bearer YOUR_CUSTOMER_TOKEN
Content-Type: application/json

{
    "customerId": 1,
    "loanType": "HOME",
    "principalAmount": 500000.00,
    "interestRate": 8.5,
    "tenureMonths": 240,
    "purpose": "Purchase of residential property"
}
```

#### 6.3 Get Loan Details

**Request:**
```http
GET http://localhost:8080/api/loans/{loanId}
Authorization: Bearer YOUR_CUSTOMER_TOKEN
```

#### 6.4 Make EMI Payment

**Request:**
```http
POST http://localhost:8080/api/loans/{loanId}/pay-emi
Authorization: Bearer YOUR_CUSTOMER_TOKEN
Content-Type: application/json

{
    "accountId": 1,
    "amount": 2361.11
}
```

#### 6.5 Get EMI Schedule

**Request:**
```http
GET http://localhost:8080/api/loans/{loanId}/emi-schedule
Authorization: Bearer YOUR_CUSTOMER_TOKEN
```

---

## Joint Account Management

### 🤝 Understanding Joint Accounts

Joint accounts allow multiple customers to share ownership and access to a single bank account. Features include:

- **Primary Holder**: Account creator with full privileges
- **Secondary Holders**: Added members with transaction privileges
- **Shared Access**: All holders can perform transactions
- **Email Notifications**: All holders receive transaction alerts

### Step 1: Create Joint Account

#### 1.1 Create Joint Savings Account

**Request:**
```http
POST http://localhost:8080/api/accounts/joint
Authorization: Bearer PRIMARY_CUSTOMER_TOKEN
Content-Type: application/json

{
    "primaryCustomerId": 1,
    "secondaryCustomerIds": [2, 3],
    "accountType": "JOINT_SAVINGS",
    "initialDeposit": 5000.00
}
```

**Expected Response:**
```json
{
    "success": true,
    "message": "Joint account created successfully",
    "data": {
        "id": 5,
        "accountNumber": "ACC987654321098",
        "accountType": "JOINT_SAVINGS",
        "balance": 5000.00,
        "status": "ACTIVE",
        "customerId": 1,
        "customerName": "John Doe",
        "isJointAccount": true,
        "jointHolders": [
            {
                "id": 1,
                "customerId": 2,
                "customerName": "Jane Smith",
                "customerEmail": "jane.smith@example.com",
                "holderRole": "SECONDARY",
                "addedDate": "2024-01-15T15:30:00",
                "isActive": true
            },
            {
                "id": 2,
                "customerId": 3,
                "customerName": "Bob Johnson",
                "customerEmail": "bob.johnson@example.com",
                "holderRole": "SECONDARY",
                "addedDate": "2024-01-15T15:30:00",
                "isActive": true
            }
        ]
    }
}
```

### Step 2: Manage Joint Account Holders

#### 2.1 Get Joint Account Holders

**Request:**
```http
GET http://localhost:8080/api/accounts/{jointAccountId}/holders
Authorization: Bearer ANY_HOLDER_TOKEN
```

#### 2.2 Add New Joint Holder

**Request:**
```http
POST http://localhost:8080/api/accounts/joint/add-holder
Authorization: Bearer PRIMARY_HOLDER_TOKEN
Content-Type: application/json

{
    "accountId": 5,
    "customerId": 4
}
```

#### 2.3 Remove Joint Holder

**Request:**
```http
DELETE http://localhost:8080/api/accounts/5/holders
Authorization: Bearer PRIMARY_HOLDER_TOKEN
Content-Type: application/json

{
    "accountId": 5,
    "customerId": 3
}
```

### Step 3: Joint Account Transactions

All standard transaction operations work with joint accounts:

#### 3.1 Deposit by Any Holder

**Request:**
```http
POST http://localhost:8080/api/transactions/deposit
Authorization: Bearer ANY_HOLDER_TOKEN
Content-Type: application/json

{
    "accountId": 5,
    "amount": 1000.00,
    "description": "Monthly contribution"
}
```

#### 3.2 Transfer from Joint Account

**Request:**
```http
POST http://localhost:8080/api/transactions/transfer
Authorization: Bearer ANY_HOLDER_TOKEN
Content-Type: application/json

{
    "fromAccountId": 5,
    "toAccountId": 1,
    "amount": 500.00,
    "description": "Transfer to personal account"
}
```

---

## Advanced Features

### 📧 Email Notifications

The system automatically sends email notifications for:

- **All Transactions** (Deposit, Withdrawal, Transfer)
- **Loan Operations** (Approval, EMI payments)
- **Fixed Deposit Events** (Creation, Maturity, Closure)
- **Joint Account Activities** (Holder addition/removal)

**Email Recipients:**
- **Individual Accounts**: Account owner
- **Joint Accounts**: All active holders

### 🔒 Access Control Features

#### Customer Access Rules:
- Can only access own accounts and data
- Joint account holders can access shared accounts
- Cannot view other customers' information

#### Admin Access Rules:
- **ADMIN**: Customer and account management
- **SUPER_ADMIN**: Full system access including admin management

### ⚡ Real-time Features

- **Balance Updates**: Immediate balance updates after transactions
- **Transaction History**: Real-time transaction logging
- **Status Changes**: Instant account/loan status updates

---

## Testing Scenarios

### 🧪 Scenario 1: Complete Customer Journey

1. **Register New Customer**
2. **Login and Get Profile**
3. **Create Savings Account**
4. **Make Initial Deposit**
5. **Create Fixed Deposit**
6. **Apply for Loan**
7. **Check Email Notifications**

### 🧪 Scenario 2: Joint Account Workflow

1. **Register Multiple Customers**
2. **Primary Customer Creates Joint Account**
3. **Verify All Holders Receive Notifications**
4. **Secondary Holder Makes Transaction**
5. **Add New Holder**
6. **Remove a Holder**

### 🧪 Scenario 3: Admin Operations

1. **Admin Login**
2. **View Pending Customer Registrations**
3. **Approve Customer Registration**
4. **Reject Customer Registration**
5. **View Customer Reports**
6. **Approve Loan Application**
7. **Generate Reports**
8. **Create New Admin**

### 🧪 Scenario 4: Error Handling

1. **Invalid Login Credentials**
2. **Insufficient Balance Withdrawal**
3. **Access Other Customer's Data**
4. **Invalid Account Numbers**

---

## Troubleshooting

### Common Issues and Solutions

#### Issue 1: Authentication Failed
**Symptoms:** 401 Unauthorized responses
**Solutions:**
- Verify token is included in Authorization header
- Check token expiration (24 hours)
- Re-login to get fresh token

#### Issue 2: Access Denied
**Symptoms:** 403 Forbidden responses  
**Solutions:**
- Verify user role permissions
- Check customer ID matches token
- Ensure admin privileges for admin operations

#### Issue 3: Account Not Found
**Symptoms:** 404 Not Found for accounts
**Solutions:**
- Verify account exists and is active
- Check account ownership/access rights
- Use correct account ID in requests

#### Issue 4: Insufficient Balance
**Symptoms:** Transaction fails with balance error
**Solutions:**
- Check current account balance
- Ensure sufficient funds for withdrawal/transfer
- Account for any holds or freezes

#### Issue 5: Email Notifications Not Working
**Symptoms:** No emails received
**Solutions:**
- Check email configuration in application.properties
- Verify Gmail app password setup
- Check spam/junk folders

### Debug Commands

```bash
# Check application logs
tail -f logs/banking-app.log

# Verify database connection
curl http://localhost:8080/actuator/health

# Check active profiles
curl http://localhost:8080/actuator/env | grep "activeProfiles"

# Monitor JVM metrics
curl http://localhost:8080/actuator/metrics
```

---

## API Reference

### Base URL
```
http://localhost:8080/api
```

### Authentication Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Customer registration |
| POST | `/auth/login` | Customer login |
| POST | `/auth/admin/login` | Admin login |

### Customer Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/customers/profile` | Get customer profile |
| PUT | `/customers/profile` | Update customer profile |
| PUT | `/customers/verify-email` | Verify email address |
| PUT | `/customers/verify-phone` | Verify phone number |

### Account Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/accounts` | Create new account |
| GET | `/accounts/{id}` | Get account details |
| GET | `/accounts/{id}/balance` | Get account balance |
| GET | `/accounts/customer/{customerId}` | Get customer accounts |
| PUT | `/accounts/{id}/close` | Close account |
| PUT | `/accounts/{id}/reopen` | Reopen account |

### Joint Account Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/accounts/joint` | Create joint account |
| GET | `/accounts/{id}/joint-holders` | Get joint holders |
| POST | `/accounts/joint/add-holder` | Add joint holder |
| DELETE | `/accounts/joint/remove-holder` | Remove joint holder |

### Transaction Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/transactions/deposit` | Deposit money |
| POST | `/transactions/withdraw` | Withdraw money |
| POST | `/transactions/transfer` | Transfer money |
| GET | `/transactions/account/{accountId}` | Get transaction history |

### Fixed Deposit Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/fixed-deposits` | Create fixed deposit |
| GET | `/fixed-deposits/{id}` | Get FD details |
| GET | `/fixed-deposits/customer/{customerId}` | Get customer FDs |
| PUT | `/fixed-deposits/{id}/close` | Close FD |
| PUT | `/fixed-deposits/{id}/auto-renewal` | Set auto-renewal |

### Loan Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/loans` | Apply for loan |
| GET | `/loans/{id}` | Get loan details |
| GET | `/loans/customer/{customerId}` | Get customer loans |
| PUT | `/loans/{id}/approve` | Approve loan (Admin) |
| PUT | `/loans/{id}/reject` | Reject loan (Admin) |
| POST | `/loans/{id}/pay-emi` | Make EMI payment |

### Admin Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/admin/customers` | Get all customers |
| GET | `/admin/customers/pending` | Get pending customers |
| GET | `/admin/customers/{id}` | Get customer details |
| PUT | `/admin/customers/{id}/activate` | Activate customer |
| PUT | `/admin/customers/{id}/deactivate` | Deactivate customer |
| PUT | `/admin/customers/{id}/approve` | Approve customer registration |
| PUT | `/admin/customers/{id}/reject` | Reject customer registration |
| POST | `/admin/admins` | Create new admin |
| PUT | `/admin/admins/{id}/role` | Update admin role |

### Report Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/reports/account-balances` | Account balance report |
| GET | `/reports/transaction-volume` | Transaction volume report |
| GET | `/reports/customer-activity` | Customer activity report |
| GET | `/reports/most-active-accounts` | Most active accounts |

---

## Status Codes

| Code | Description |
|------|-------------|
| 200 | Success |
| 201 | Created |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 500 | Internal Server Error |

---

## Support

For technical support or questions:

1. **Check Application Logs**: `logs/banking-app.log`
2. **Review API Documentation**: This guide
3. **Test with Postman Collection**: Use provided collection
4. **Verify Database State**: Check MySQL/H2 console

---

**Last Updated:** September 27, 2025  
**Version:** 2.0.0  
**Author:** Banking Application Team
