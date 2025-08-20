# Admin Login Fix

## Problem
The admin login was failing because the `admin` table doesn't exist in the database or doesn't have any admin records.

## Solution

### Step 1: Run the Database Setup Script
Execute the `admin_setup.sql` script in your MySQL database:

```sql
-- Connect to your MySQL database and run:
source admin_setup.sql
```

Or manually run these commands:

```sql
USE tss_students;

-- Create admin table
CREATE TABLE IF NOT EXISTS admin (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default admin user
INSERT INTO admin (username, password) VALUES ('admin', 'admin123')
ON DUPLICATE KEY UPDATE username = username;
```

### Step 2: Default Admin Credentials
After running the script, you can login with:
- **Username:** `admin`
- **Password:** `admin123`

### Step 3: Verify the Setup
Check if the admin table was created and has data:

```sql
USE tss_students;
SELECT * FROM admin;
```

### Step 4: Test the Login
1. Start your application
2. Go to the login page
3. Select "Admin" from the dropdown
4. Enter username: `admin` and password: `admin123`
5. Click "Secure Login"

## Debugging
I've added console logging to help debug any remaining issues:

- **AdminController**: Logs login attempts and results
- **AdminDAO**: Logs database queries and validation results

Check your application console/logs for these messages:
- `AdminController: Login attempt for username: [username]`
- `AdminDAO: Attempting to validate admin with username: [username]`
- `AdminDAO: Admin validation successful for user: [username]` (if successful)
- `AdminDAO: No admin found with username: [username]` (if failed)

## Database Connection
Make sure your database connection is working. The application uses:
- **Database:** `tss_students`
- **Username:** `root`
- **Password:** `root`
- **Host:** `localhost:3306`

If your database credentials are different, update them in `src/main/java/com/tss/util/DBConnection.java`.

## Security Note
In a production environment, you should:
1. Use hashed passwords instead of plain text
2. Change the default admin credentials
3. Implement proper session management
4. Add rate limiting for login attempts
