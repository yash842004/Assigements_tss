<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Customer Registration</title>
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
  background: linear-gradient(135deg, #1e90ff, #00c6ff);
  transition: background 0.6s ease;
}

/* Card */
.card {
  border-radius: 20px;
  box-shadow: 0 10px 40px rgba(0,0,0,0.3);
  width: 100%;
  max-width: 450px;
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
.btn-register {
  border-radius: 10px;
  font-weight: bold;
  background-color: #0056b3;
  border: none;
}
.btn-register:hover {
  background-color: #004494;
}
.login-link {
  display: block;
  margin-top: 15px;
  text-align: center;
  font-weight: 500;
  color: #0056b3;
  text-decoration: none;
}
.login-link:hover {
  text-decoration: underline;
}
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
</head>
<body>

<div class="card">
  <h3>✨ Create Your Account</h3>

  <!-- Success Message -->
  <c:if test="${not empty successMessage}">
    <div class="alert alert-success text-center">${successMessage}</div>
  </c:if>

  <form action="register" method="post">
    <div class="mb-3">
      <label class="form-label" for="fullName">Full Name</label>
      <input type="text" class="form-control" id="fullName" name="fullName" required>
    </div>

    <div class="mb-3">
      <label class="form-label" for="email">Email</label>
      <input type="email" class="form-control" id="email" name="email" required>
    </div>

    <div class="mb-3">
      <label class="form-label" for="password">Password</label>
      <input type="password" class="form-control" id="password" name="password" required>
    </div>

    <div class="mb-3">
      <label class="form-label" for="address">Address</label>
      <input type="text" class="form-control" id="address" name="address" required>
    </div>

    <div class="mb-3">
      <label class="form-label" for="phone">Phone Number</label>
      <input type="text" class="form-control" id="phone" name="phone" required>
    </div>

    <button type="submit" class="btn btn-register w-100">Register</button>
  </form>

  <a href="index.jsp" class="login-link">Already have an account? Login</a>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
