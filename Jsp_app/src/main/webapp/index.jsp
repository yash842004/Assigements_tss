<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>JSP Actions Demo</h2>

	<jsp:useBean id="stud" class="com.tss.model.Student" scope="request" />
	<jsp:setProperty name="stud" property="name" value="Yash" />
	<jsp:setProperty name="stud" property="marks" value="90" />

	<h3>Bean Example:</h3>
	<p>
		Name:
		<jsp:getProperty name="stud" property="name" /></p>
	<p>
		Marks:
		<jsp:getProperty name="stud" property="marks" /></p>

	<hr>


	<h3>Include Example:</h3>
	<jsp:include page="course.jsp">
		<jsp:param name="course" value="Java" />
		<jsp:param name="duration" value="6 Months" />
	</jsp:include>

	<hr>

	<h3>Forward Example:</h3>
	<p>
		<a href="index.jsp?role=admin">Forward to Admin</a> | <a
			href="index.jsp?role=student">Forward to Student</a>
	</p>

	<%
	String role = request.getParameter("role");
	if (role != null) {
		String msg = "Welcome " + role.substring(0, 1).toUpperCase() + role.substring(1) + "!";
	%>
	<jsp:forward page="helper.jsp">
		<jsp:param name="message" value="<%=msg%>" />
	</jsp:forward>
	<%
	}
	%>



</body>
</html>