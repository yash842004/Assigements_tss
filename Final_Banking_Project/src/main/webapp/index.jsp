<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AuraBank - Secure Login</title>

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <!-- Google Fonts: Poppins -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">

    <style>
        :root {
            --primary-color: #6c63ff;
            --dark-bg: #121212;
            --form-bg: #1e1e1e;
            --text-color: #e0e0e0;
            --muted-color: #888;
            --border-color: #333;
        }

        body {
            font-family: 'Poppins', sans-serif;
            margin: 0;
            height: 100vh;
            overflow: hidden;
            background-color: var(--dark-bg);
            color: var(--text-color);
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .login-container {
            display: flex;
            width: 100%;
            height: 100%;
            max-width: 1200px;
            max-height: 700px;
            background-color: var(--form-bg);
            border-radius: 20px;
            box-shadow: 0 15px 40px rgba(0,0,0,0.5);
            overflow: hidden;
            animation: fadeIn 1s ease-out;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: scale(0.98); }
            to { opacity: 1; transform: scale(1); }
        }

        /* --- Left Panel (Illustration) --- */
        .illustration-panel {
            flex: 1;
            background: linear-gradient(160deg, var(--primary-color), #4a40d1);
            position: relative;
            display: flex;
            align-items: center;
            justify-content: center;
            overflow: hidden;
        }
        
        .illustration-panel .shape {
            position: absolute;
            background-color: rgba(255, 255, 255, 0.1);
            border-radius: 50%;
            animation: float 15s infinite ease-in-out;
        }
        .shape1 { width: 200px; height: 200px; top: 10%; left: 15%; animation-delay: 0s; }
        .shape2 { width: 80px; height: 80px; top: 70%; left: 25%; animation-delay: 3s; }
        .shape3 { width: 120px; height: 120px; top: 40%; left: 70%; animation-delay: 6s; }
        .shape4 { width: 50px; height: 50px; top: 20%; left: 80%; animation-delay: 9s; }
        
        @keyframes float {
            0%, 100% { transform: translateY(0) rotate(0deg); }
            50% { transform: translateY(-30px) rotate(180deg); }
        }

        .illustration-panel .main-icon {
            font-size: 10rem;
            color: rgba(255, 255, 255, 0.8);
            z-index: 10;
            text-shadow: 0 10px 25px rgba(0,0,0,0.2);
        }

        /* --- Right Panel (Form) --- */
        .form-panel {
            flex: 1;
            padding: 4rem;
            display: flex;
            flex-direction: column;
            justify-content: center;
        }

        .form-panel h2 {
            font-weight: 700;
            font-size: 2.5rem;
            margin-bottom: 0.5rem;
        }
        .form-panel p {
            color: var(--muted-color);
            margin-bottom: 2.5rem;
        }

        .form-group {
            position: relative;
            margin-bottom: 2rem;
        }
        
        .form-control, .form-select {
            background-color: transparent;
            border: none;
            border-bottom: 2px solid var(--border-color);
            border-radius: 0;
            padding: 0.5rem 0;
            font-size: 1rem;
            color: var(--text-color);
            transition: border-color 0.3s ease;
        }
        .form-control:focus, .form-select:focus {
            box-shadow: none;
            border-color: var(--primary-color);
        }
        .form-select {
            -webkit-appearance: none;
            -moz-appearance: none;
            appearance: none;
            background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16'%3e%3cpath fill='none' stroke='%23888' stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='m2 5 6 6 6-6'/%3e%3c/svg%3e");
            background-repeat: no-repeat;
            background-position: right 0.5rem center;
            background-size: 16px 12px;
        }
        .form-select option { background-color: var(--form-bg); }

        /* --- Button Styling --- */
        .btn-login {
            border-radius: 8px;
            font-weight: 600;
            padding: 0.8rem;
            transition: all 0.3s ease;
            background-color: var(--primary-color);
            border: none;
            width: 100%;
        }
        
        .btn-login:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 20px rgba(108, 99, 255, 0.3);
        }
        
        #registerLink {
            text-align: center;
            margin-top: 1.5rem;
            transition: opacity 0.5s ease;
        }
        #registerLink a {
            color: var(--primary-color);
            text-decoration: none;
            font-weight: 500;
        }

        /* --- Responsive Design --- */
        @media (max-width: 992px) {
            .illustration-panel {
                display: none;
            }
            .form-panel {
                padding: 2.5rem;
            }
            .login-container {
                max-height: 100%;
                height: auto;
                margin: 2rem;
            }
        }
    </style>
</head>
<body>

    <div class="login-container">
        <!-- Left Panel with Illustration -->
        <div class="illustration-panel">
            <div class="shape shape1"></div>
            <div class="shape shape2"></div>
            <div class="shape shape3"></div>
            <div class="shape shape4"></div>
            <i class="bi bi-shield-lock-fill main-icon"></i>
        </div>

        <!-- Right Panel with Form -->
        <div class="form-panel">
            <h2>Welcome Back!</h2>
            <p id="form-subtitle">Login as a Customer to access your account.</p>

            <form id="loginForm" method="post" action="login">
                <div class="form-group">
                    <select id="roleSelect" class="form-select" required>
                        <option value="customer" selected>Login as Customer</option>
                        <option value="admin">Login as Admin</option>
                    </select>
                </div>

                <div class="form-group">
                    <input type="text" class="form-control" id="usernameField" name="email" placeholder="Email Address" required>
                </div>

                <div class="form-group">
                    <input type="password" class="form-control" name="password" placeholder="Password" required>
                </div>

                <button type="submit" class="btn btn-primary btn-login" id="loginBtn">
                    <i class="bi bi-box-arrow-in-right me-2"></i>Secure Login
                </button>
            </form>

            <div id="registerLink">
                <p>Don't have an account? <a href="register.jsp">Register Here</a></p>
            </div>

            <%
            String error = (String) request.getAttribute("errorMessage");
            if (error != null) {
            %>
            <div class="alert alert-danger mt-3"><%=error%></div>
            <%
            }
            %>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const roleSelect = document.getElementById("roleSelect");
            const formSubtitle = document.getElementById("form-subtitle");
            const loginForm = document.getElementById("loginForm");
            const usernameField = document.getElementById("usernameField");
            const registerLink = document.getElementById("registerLink");

            // --- Role Switcher Logic ---
            roleSelect.addEventListener("change", function() {
                if (this.value === "admin") {
                    formSubtitle.textContent = "Admin Portal: Please enter your credentials.";
                    loginForm.action = "adminLogin";
                    usernameField.name = "username";
                    usernameField.placeholder = "Admin Username";
                    registerLink.style.opacity = "0";
                } else {
                    formSubtitle.textContent = "Login as a Customer to access your account.";
                    loginForm.action = "login";
                    usernameField.name = "email";
                    usernameField.placeholder = "Email Address";
                    registerLink.style.opacity = "1";
                }
            });
        });
    </script>
</body>
</html>
