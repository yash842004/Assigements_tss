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

	<h3>All Users</h3>
	<table>
		<tr>
			<th>Name</th>
			<th>Password</th>
		</tr>
		<c:forEach var="user" items="${userList}">
			<tr>
				<td>${user.username}</td>
				<td>${user.password}</td>
			</tr>
		</c:forEach>
	</table>

	<c:if test="${empty userList}">
		<p>No users found.</p>
	</c:if>


</body>
</html>