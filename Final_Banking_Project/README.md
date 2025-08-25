# AuraBank - Banking Application

A comprehensive Java web-based banking application with customer and admin functionalities.

## 🚀 Features

### Customer Features
- **User Registration & Login**: Secure customer registration and authentication
- **Account Management**: Open multiple accounts (Savings, Checking)
- **Transaction Operations**:
  - **Deposit**: Add funds to any account (max $1,000,000 per transaction)
  - **Withdraw**: Remove funds from accounts (max $50,000 per transaction)
  - **Transfer**: Transfer between accounts (max $100,000 per transaction)
- **Passbook**: View transaction history for all accounts
- **Account Management**: Update account types and delete accounts
- **Real-time Balance Updates**: All transactions update balances immediately

### Admin Features
- **Admin Login**: Secure admin authentication
- **Customer Management**: Approve, reject, activate, and deactivate customers
- **Transaction Monitoring**: View all customer transactions
- **Account Overview**: Monitor all customer accounts and balances
- **Multi-Account Support**: View transactions for any customer account

## 🔧 Technical Stack

- **Backend**: Java Servlets, JSP
- **Database**: MySQL
- **Frontend**: Bootstrap 5, JSTL
- **Architecture**: MVC Pattern
- **Transaction Management**: Database transactions with rollback support

## 📋 Prerequisites

- Java 8 or higher
- MySQL 8.0 or higher
- Apache Tomcat 9.0 or higher
- Maven (for dependency management)

## 🗄️ Database Setup

### 1. Create Database
```sql
CREATE DATABASE tss_students;
USE tss_students;
```

### 2. Create Tables
```sql
-- Customers table
CREATE TABLE customers (
    customerId INT PRIMARY KEY AUTO_INCREMENT,
    fullName VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    address TEXT,
    phone VARCHAR(20),
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'DEACTIVATED') DEFAULT 'PENDING',
    createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Admin table
CREATE TABLE admin (
    adminId INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Accounts table
CREATE TABLE accounts (
    accountId INT PRIMARY KEY AUTO_INCREMENT,
    accountNumber VARCHAR(50) UNIQUE NOT NULL,
    customerId INT NOT NULL,
    accountType ENUM('SAVINGS', 'CHECKING') NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00,
    createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customerId) REFERENCES customers(customerId) ON DELETE CASCADE
);

-- Transactions table
CREATE TABLE transactions (
    transactionId INT PRIMARY KEY AUTO_INCREMENT,
    accountId INT NOT NULL,
    transactionType ENUM('DEPOSIT', 'WITHDRAWAL', 'TRANSFER_IN', 'TRANSFER_OUT') NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    description TEXT,
    transactionDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (accountId) REFERENCES accounts(accountId) ON DELETE CASCADE
);
```

### 3. Insert Default Admin
```sql
INSERT INTO admin (username, password) VALUES ('admin', 'admin123');
```

## 🚀 Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd Final_Banking_Project
```

### 2. Configure Database Connection
Edit `src/main/java/com/tss/util/DBConnection.java`:
```java
private static final String url = "jdbc:mysql://localhost:3306/tss_students?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
private static final String username = "your_username";
private static final String password = "your_password";
```

### 3. Deploy to Tomcat
1. Build the project
2. Deploy the WAR file to Tomcat
3. Access the application at `http://localhost:8080/your-app-name`

## 🧪 Testing Guide

### Customer Testing

#### 1. Registration & Login
1. Access the application
2. Click "Register" and create a new account
3. Wait for admin approval
4. Login with registered credentials

#### 2. Account Operations
1. **Open Account**: Go to "Manage Accounts" → "Open New Account"
2. **Deposit**: Click "Deposit Money" → Select account → Enter amount
3. **Withdraw**: Click "Withdraw Money" → Select account → Enter amount
4. **Transfer**: Click "Transfer Money" → Select source account → Enter destination account number → Enter amount

#### 3. Transaction History
1. Click "Passbook" to view transaction history
2. Use account selector to switch between accounts
3. Verify all transactions are recorded correctly

#### 4. Account Management
1. Go to "Manage Accounts"
2. Update account types
3. Delete accounts (only if balance is zero)

### Admin Testing

#### 1. Admin Login
1. Use credentials: `admin` / `admin123`
2. Verify dashboard loads with customer list

#### 2. Customer Management
1. **Approve Customer**: Click "Approve" for pending customers
2. **Reject Customer**: Click "Reject" for unwanted registrations
3. **Activate/Deactivate**: Manage existing customer status

#### 3. Transaction Monitoring
1. Click "View Transactions" for any customer
2. Select different accounts to view transaction history
3. Verify all transaction details are displayed correctly

## 🔒 Security Features

- **Input Validation**: All user inputs are validated
- **SQL Injection Prevention**: Prepared statements used throughout
- **Session Management**: Secure session handling with timeouts
- **Transaction Integrity**: Database transactions with rollback support
- **Access Control**: Customers can only access their own accounts

## 🛠️ Recent Fixes & Improvements

### Transaction Operations
- ✅ **Enhanced TransactionController**: Added comprehensive validation and error handling
- ✅ **Transaction Management**: All operations now use database transactions
- ✅ **Amount Limits**: Implemented reasonable limits for deposits, withdrawals, and transfers
- ✅ **Balance Validation**: Real-time balance checking before operations
- ✅ **Success/Error Messages**: Clear feedback for all operations

### Account Management
- ✅ **Multi-Account Support**: Customers can have multiple accounts
- ✅ **Account Deletion**: Fixed foreign key constraint issues
- ✅ **Transaction Cleanup**: Associated transactions are deleted with accounts
- ✅ **Balance Validation**: Cannot delete accounts with remaining balance

### Admin Functions
- ✅ **Enhanced AdminController**: Better error handling and validation
- ✅ **Customer Management**: Improved approve/reject/activate/deactivate functions
- ✅ **Transaction Viewing**: Enhanced transaction monitoring capabilities
- ✅ **Multi-Account Support**: Admin can view transactions for any customer account

### User Experience
- ✅ **Success/Error Messages**: Added to customer dashboard
- ✅ **Form Validation**: Client-side and server-side validation
- ✅ **Responsive Design**: Mobile-friendly interface
- ✅ **Account Selector**: Easy switching between accounts in passbook

## 🐛 Known Issues & Solutions

### Issue: "Cannot delete account with remaining balance"
**Solution**: Withdraw all funds before deleting the account.

### Issue: "Insufficient funds for withdrawal"
**Solution**: Check account balance before attempting withdrawal.

### Issue: "Destination account not found"
**Solution**: Verify the recipient account number is correct.

### Issue: "Transfer amount exceeds limit"
**Solution**: Respect the transaction limits (deposit: $1M, withdraw: $50K, transfer: $100K).

## 📞 Support

For technical support or questions:
1. Check the console logs for detailed error messages
2. Verify database connection settings
3. Ensure all prerequisites are installed
4. Check Tomcat logs for deployment issues

## 🔄 Version History

### v2.0 (Current)
- Complete transaction functionality
- Multi-account support
- Enhanced admin features
- Improved error handling
- Security improvements

### v1.0
- Basic customer and admin functionality
- Single account support
- Basic transaction operations

---

**Note**: This application is for educational purposes. In a production environment, additional security measures such as password hashing, HTTPS, and more robust authentication should be implemented.
