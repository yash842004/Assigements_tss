<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Register</h1>
	<form action="/register" method="post">
		<!-- Assume RegisterServlet -->
		Username: <input type="text" name="username"><br>
		Password: <input type="password" name="password"><br>
		Full Name: <input type="text" name="fullName"><br> Email:
		<input type="email" name="email"><br> Account Type: <select
			name="accountType">
			<option value="savings">Savings</option>
			<option value="current">Current</option>
			<option value="fixed_deposit">Fixed Deposit</option>
		</select><br> <input type="submit" value="Register">
	</form>
</body>
</html>