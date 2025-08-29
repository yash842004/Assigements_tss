<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AuraBank - Create Account</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">

    <style>
        :root {
            --primary-color: #6c63ff;
            --dark-bg: #121212;
            --form-bg: #1e1e1e;
            --text-color: #e0e0e0;
            --muted-color: #aaa;
            --border-color: #444;
            --accent-color: #ff6584;
            --success-color: #4caf50;
            --error-color: #f44336;
        }

        body {
            font-family: 'Poppins', sans-serif;
            margin: 0;
            min-height: 100vh;
            background-color: var(--dark-bg);
            color: var(--text-color);
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .register-container {
            display: flex;
            flex: 1;
            width: 100%;
            max-width: 1100px;
            height: 100vh;              /* full screen height */
            background-color: var(--form-bg);
            border-radius: 0;           /* no rounding so it fits nicely */
            box-shadow: 0 10px 30px rgba(0,0,0,0.6);
            overflow: hidden;
        }

        /* Left Illustration */
        .illustration-panel {
            flex: 1;
            background: linear-gradient(160deg, var(--primary-color), #4a40d1);
            display: flex;
            align-items: center;
            justify-content: center;
            position: relative;
        }
        .illustration-panel i {
            font-size: 8rem;
            color: rgba(255,255,255,0.85);
            animation: pulse 2s infinite;
        }
        @keyframes pulse {
            0% { transform: scale(1);}
            50% { transform: scale(1.05);}
            100% { transform: scale(1);}
        }

        /* Right Panel */
        .form-panel {
            flex: 1;
            padding: 2.5rem;
            display: flex;
            flex-direction: column;
            justify-content: center;
            overflow-y: auto;   /* only form scrolls if needed */
        }

        .form-panel h2 {
            color: var(--primary-color);
            font-weight: 700;
            margin-bottom: 0.5rem;
        }
        .form-panel p {
            color: var(--muted-color);
            margin-bottom: 2rem;
        }

        .form-group {
            margin-bottom: 1.2rem;
        }
        .form-label {
            font-size: 0.9rem;
            color: var(--muted-color);
            margin-bottom: 0.3rem;
        }

        .form-control {
            background-color: transparent;
            border: none;
            border-bottom: 2px solid var(--border-color);
            border-radius: 0;
            padding: 0.6rem 0;
            font-size: 1rem;
            color: var(--text-color);
        }
        .form-control:focus {
            box-shadow: none;
            border-color: var(--primary-color);
        }

        .invalid-feedback {
            font-size: 0.85rem;
            color: var(--error-color);
        }

        .btn-register {
            border-radius: 8px;
            font-weight: 600;
            padding: 0.8rem;
            transition: all 0.3s ease;
            background-color: var(--primary-color);
            border: none;
            width: 100%;
            margin-top: 1rem;
        }
        .btn-register:hover {
            transform: translateY(-3px);
            background-color: #5a55d9;
        }

        .login-link {
            text-align: center;
            margin-top: 1.5rem;
        }
        .login-link a {
            color: var(--primary-color);
            text-decoration: none;
        }
        .login-link a:hover {
            color: var(--accent-color);
        }

        /* Responsive */
        @media (max-width: 992px) {
            .illustration-panel { display: none; }
            .register-container { border-radius: 0; }
        }
    </style>
</head>
<body>

<div class="register-container">
    <!-- Left Panel -->
    <div class="illustration-panel">
        <i class="bi bi-person-plus-fill"></i>
    </div>

    <!-- Right Panel -->
    <div class="form-panel">
        <h2>Create Your Account</h2>
        <p>Join AuraBank and start your premium banking journey.</p>

        <c:if test="${not empty successMessage}">
            <div class="alert alert-success text-center">${successMessage}</div>
        </c:if>

        <form id="registrationForm" action="${pageContext.request.contextPath}/register" method="post" novalidate>
            <div class="form-group">
                <label for="fullName" class="form-label">Full Name</label>
                <input type="text" class="form-control" id="fullName" name="fullName" required>
                <div class="invalid-feedback">Please enter your full name.</div>
            </div>
            <div class="form-group">
                <label for="email" class="form-label">Email Address</label>
                <input type="email" class="form-control" id="email" name="email" required>
                <div class="invalid-feedback">Please enter a valid email address.</div>
            </div>
            <div class="form-group">
                <label for="password" class="form-label">Password</label>
                <input type="password" class="form-control" id="password" name="password" required minlength="8">
                <div class="invalid-feedback">Password must be at least 8 characters long.</div>
            </div>
            <div class="form-group">
                <label for="address" class="form-label">Address</label>
                <input type="text" class="form-control" id="address" name="address" required>
                <div class="invalid-feedback">Please enter your address.</div>
            </div>
            <div class="form-group">
                <label for="phone" class="form-label">Phone Number</label>
                <input type="tel" class="form-control" id="phone" name="phone" required pattern="[0-9]{10,15}">
                <div class="invalid-feedback">Please enter a valid phone number (10-15 digits).</div>
            </div>
            <button type="submit" class="btn btn-primary btn-register">Register</button>
        </form>

        <div class="login-link">
            <p>Already have an account? <a href="index.jsp">Login Here</a></p>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<script>
    // Bootstrap Validation
    (function () {
        'use strict'
        var form = document.querySelector('#registrationForm');
        form.addEventListener('submit', function (event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    })()
</script>

<c:if test="${not empty successMessage}">
    <script>
        window.onload = function () {
            alert("${successMessage}");
        }
    </script>
</c:if>

</body>
</html>
