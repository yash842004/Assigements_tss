<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Account Details</h1>
	<p>Account Number: ${account.accountNumber}</p>
	<p>Type: ${account.accountType}</p>
	<p>Balance: ${account.balance}</p>
	<a href="/customer/dashboard">Back</a>
</body>
</html>