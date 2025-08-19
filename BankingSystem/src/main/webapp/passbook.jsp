<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Passbook</h1>
	<table border="1">
		<tr>
			<th>Type</th>
			<th>Amount</th>
			<th>Description</th>
			<th>Timestamp</th>
		</tr>
		<c:forEach items="${transactions}" var="tx">
			<tr>
				<td>${tx.type}</td>
				<td>${tx.amount}</td>
				<td>${tx.description}</td>
				<td>${tx.timestamp}</td>
			</tr>
		</c:forEach>
	</table>
	<a href="/customer/dashboard">Back</a>

</body>
</html>