<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.Date"%>

<!DOCTYPE html>
<html>
<head>
<title>Student Portal</title>
</head>
<body>

	<h1><%="Welcome to Student Portal"%></h1>

	<p>
		<b>Current Date & Time:</b>
		<%=new Date()%></p>

	<%!String studentName = "Yash";
	String course = "Computer Science";
	int marks = 72;
	String grade;%>

	<%
	if (marks >= 90) {
		grade = "A";
	} else if (marks >= 75 && marks < 90) {
		grade = "B";
	} else if (marks >= 50 && marks < 75) {
		grade = "C";
	} else {
		grade = "F";
	}
	%>

	<h2>Student Details</h2>
	<p>
		<b>Name:</b>
		<%=studentName%></p>
	<p>
		<b>Course:</b>
		<%=course%></p>
	<p>
		<b>Marks:</b>
		<%=marks%></p>
	<p>
		<b>Grade:</b>
		<%=grade%></p>

</body>
</html>
