<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
<style>
body {
	font-family: Arial, sans-serif;
	background-color: #f4f4f4;
	margin: 0;
	padding: 20px;
}

.container {
	max-width: 800px;
	margin: auto;
	background: #fff;
	padding: 20px;
	border-radius: 8px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	border-bottom: 1px solid #ddd;
	padding-bottom: 10px;
}

.header h1 {
	margin: 0;
	color: #333;
}

.logout {
	background-color: #dc3545;
	color: white;
	padding: 8px 15px;
	text-decoration: none;
	border-radius: 5px;
}

.account-details {
	background-color: #e7f3fe;
	border-left: 6px solid #2196F3;
	margin-top: 20px;
	padding: 15px;
}

.account-details p {
	margin: 5px 0;
	font-size: 18px;
}

.actions {
	margin-top: 20px;
	display: grid;
	grid-template-columns: 1fr 1fr;
	gap: 15px;
}

.action-card {
	background-color: #f8f9fa;
	border: 1px solid #ddd;
	padding: 20px;
	border-radius: 5px;
	text-align: center;
}

.action-card a {
	text-decoration: none;
	color: #0056b3;
	font-weight: bold;
	font-size: 18px;
}
</style>
<title>Insert title here</title>
</head>
<body>
	<div class="container">
		<div class="header">
			<h1>Welcome, ${sessionScope.customer.fullName}</h1>
			<a href="logout" class="logout">Logout</a>
		</div>

		<div class="account-details">
			<h2>Your Account Summary</h2>
			<p>
				<strong>Account Number:</strong> ${account.accountNumber}
			</p>
			<p>
				<strong>Account Type:</strong> ${account.accountType}
			</p>
			<p>
				<strong>Current Balance:</strong>
				<fmt:setLocale value="en_US" />
				<fmt:formatNumber value="${account.balance}" type="currency" />
			</p>
		</div>

		<div class="actions">
			<div class="action-card">
				<a href="deposit.jsp">Deposit Money</a>
			</div>
			<div class="action-card">
				<a href="withdraw.jsp">Withdraw Money</a>
			</div>
			<div class="action-card">
				<a href="transfer.jsp">Transfer Money</a>
			</div>
			<div class="action-card">
				<a href="passbook.jsp">View Passbook</a>
			</div>
		</div>
	</div>
</body>
</html>