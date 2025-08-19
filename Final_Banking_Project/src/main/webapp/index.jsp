<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Bank Login</title>
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- Bootstrap 5 -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
body {
  margin: 0;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.6s ease;
}

/* Customer theme */
body.customer-theme {
  background: linear-gradient(135deg, #1e90ff, #00c6ff);
}

/* Admin theme */
body.admin-theme {
  background: linear-gradient(135deg, #ff416c, #ff4b2b);
}

/* Login card */
.card {
  border-radius: 20px;
  box-shadow: 0 10px 40px rgba(0,0,0,0.3);
  width: 100%;
  max-width: 400px;
  padding: 30px;
  animation: fadeIn 0.8s ease-in-out;
}
.card h3 {
  font-weight: bold;
  margin-bottom: 20px;
  text-align: center;
}
.card input {
  border-radius: 10px;
}
.btn-login {
  border-radius: 10px;
  font-weight: bold;
}
#registerLink {
  transition: opacity 0.5s ease;
}
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
</head>
<body class="customer-theme" id="pageBody">

<div class="card text-center">
  <h3 id="formTitle">👤 Customer Login</h3>

  <form id="loginForm" method="post" action="login">
    <!-- Role selector -->
    <div class="mb-3">
      <select id="roleSelect" class="form-select" required>
        <option value="customer" selected>Customer</option>
        <option value="admin">Admin</option>
      </select>
    </div>

    <!-- Username/Email -->
    <div class="mb-3">
      <input type="text" class="form-control" id="usernameField" name="email" placeholder="Email" required>
    </div>

    <!-- Password -->
    <div class="mb-3">
      <input type="password" class="form-control" name="password" placeholder="Password" required>
    </div>

    <!-- Login button -->
    <button type="submit" class="btn btn-dark w-100 btn-login" id="loginBtn">Secure Login</button>
  </form>

  <!-- Register button (Customer only) -->
  <div id="registerLink" class="mt-3">
    <a href="register.jsp" class="btn btn-outline-light w-100">✨ New User? Register Here</a>
  </div>

  <%-- Error message --%>
  <%
    String error = (String) request.getAttribute("errorMessage");
    if (error != null) {
  %>
    <div class="alert alert-danger mt-3"><%= error %></div>
  <% } %>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<script>
const roleSelect = document.getElementById("roleSelect");
const pageBody = document.getElementById("pageBody");
const formTitle = document.getElementById("formTitle");
const loginForm = document.getElementById("loginForm");
const usernameField = document.getElementById("usernameField");
const registerLink = document.getElementById("registerLink");

roleSelect.addEventListener("change", function() {
  if (this.value === "admin") {
    pageBody.className = "admin-theme";
    formTitle.textContent = "🛡️ Admin Login";
    loginForm.action = "adminLogin";
    usernameField.name = "username";
    usernameField.placeholder = "Admin Username";
    registerLink.style.opacity = "0";   // hide smoothly
    setTimeout(() => registerLink.style.display = "none", 500);
  } else {
    pageBody.className = "customer-theme";
    formTitle.textContent = "👤 Customer Login";
    loginForm.action = "login";
    usernameField.name = "email";
    usernameField.placeholder = "Email";
    registerLink.style.display = "block";
    setTimeout(() => registerLink.style.opacity = "1", 50); // show smoothly
  }
});
</script>
</body>
</html>
