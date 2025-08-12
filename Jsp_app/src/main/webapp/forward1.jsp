<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>


<%
    String role = request.getParameter("role");
    if("admin".equalsIgnoreCase(role)){
%>
    <jsp:forward page="adminPage.jsp" />
<%
    } else {
%>
    <jsp:forward page="studentPage.jsp" />
<%
    }
%>

</body>
</html>