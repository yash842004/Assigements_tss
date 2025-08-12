<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.Date"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<%
	// scriptlet tag
	int number1 = 10;
	int number2 = 99;

	Date date = new Date();
	%>

	<b><%= date%></b>

	<%=// expresion -> direct display in jsp page
		number1 + number2%>

	<%-- declaration tag  constant value--%>
	<%!double pi = 3.14;%>

	<p>
		pi value is
		<%=pi%></p>


	<%--DIRECTIVES --%>


	<%@ include file="Welcome2.jsp"%>

</body>
</html>