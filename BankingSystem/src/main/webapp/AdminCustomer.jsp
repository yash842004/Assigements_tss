<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Customer Management</h1>
	<table border="1">
		<tr>
			<th>ID</th>
			<th>Username</th>
			<th>Full Name</th>
			<th>Email</th>
		</tr>
		<c:forEach items="${customers}" var="cust">
			<tr>
				<td>${cust.id}</td>
				<td>${cust.username}</td>
				<td>${cust.fullName}</td>
				<td>${cust.email}</td>
			</tr>
		</c:forEach>
	</table>
	<a href="/admin/dashboard">Back</a>

</body>
</html>