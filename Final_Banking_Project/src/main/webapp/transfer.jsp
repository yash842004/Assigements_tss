<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
body {
	font-family: Arial, sans-serif;
	background-color: #f4f4f4;
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
}

.form-container {
	background: #fff;
	padding: 30px;
	border-radius: 8px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
	width: 350px;
}

h2 {
	text-align: center;
	color: #333;
}

.form-group {
	margin-bottom: 20px;
}

label {
	display: block;
	margin-bottom: 5px;
	color: #555;
}

input[type="text"], input[type="number"] {
	width: 100%;
	padding: 10px;
	border: 1px solid #ddd;
	border-radius: 4px;
	box-sizing: border-box;
}

.btn {
	width: 100%;
	background-color: #0056b3;
	color: white;
	padding: 12px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-size: 16px;
}

.btn:hover {
	background-color: #004494;
}

.error {
	color: #dc3545;
	text-align: center;
	margin-bottom: 15px;
}

.back-link {
	display: block;
	text-align: center;
	margin-top: 20px;
	color: #0056b3;
}
</style>
</head>
<body>
	<div class="form-container">
		<h2>Transfer Funds</h2>
		<c:if test="${not empty errorMessage}">
			<p class="error">${errorMessage}</p>
		</c:if>
		<form action="transfer" method="post">
			<div class="form-group">
				<label for="toAccountNumber">Recipient's Account Number:</label> <input
					type="text" id="toAccountNumber" name="toAccountNumber" required>
			</div>
			<div class="form-group">
				<label for="amount">Amount:</label> <input type="number" id="amount"
					name="amount" step="0.01" min="0.01" required>
			</div>
			<button type="submit" class="btn">Transfer</button>
		</form>
		<a href="dashboard" class="back-link">Back to Dashboard</a>
	</div>
</body>
</html>