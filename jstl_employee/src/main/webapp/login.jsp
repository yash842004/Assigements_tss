<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Leave Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700&family=Montserrat:wght@600&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Lato', sans-serif;
            background: linear-gradient(135deg, #C3B1E1, #BEE3DB);
            color: #2E2E2E;
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            margin: 0;
        }
        h2 {
            font-family: 'Montserrat', sans-serif;
        }
        .login-container {
            background-color: #FAF7F0;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
        }
        .btn-primary {
            background-color: #C3B1E1;
            border-color: #C3B1E1;
            transition: background-color 0.3s ease;
        }
        .btn-primary:hover {
            background-color: #B19CD7;
            border-color: #B19CD7;
        }
        .form-label {
            font-weight: 500;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <h2 class="text-center mb-4">Login</h2>
        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="mb-3">
                <label for="username" class="form-label">Username</label>
                <input type="text" class="form-control" id="username" name="username" required>
            </div>
            <div class="mb-3">
                <label for="password" class="form-label">Password</label>
                <input type="password" class="form-control" id="password" name="password" required>
            </div>
            <div class="mb-3">
                <label for="role" class="form-label">Role</label>
                <select class="form-select" id="role" name="role" required>
                    <option value="">Select Role</option>
                    <option value="employee">Employee</option>
                    <option value="admin">Admin</option>
                </select>
            </div>
            <button type="submit" class="btn btn-primary w-100">Login</button>
        </form>
        <c:if test="${not empty error}">
            <p style="color:red; text-align:center; margin-top:10px">${error}</p>
        </c:if>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>