<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<p>Course: <%= request.getParameter("course") %></p>
<p>Duration: <%= request.getParameter("duration") %></p>


</body>
</html>