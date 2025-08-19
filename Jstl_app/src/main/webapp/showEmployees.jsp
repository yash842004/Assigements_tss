<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Show Employees</title>
</head>
<body>
    <h1>Employee Management</h1>
    <p>Click the link below to view all employees.</p>

    <!-- Build correct URL with context path -->
    <c:url value="/viewEmployees" var="viewEmpUrl" />
    <a href="${viewEmpUrl}">View Employees</a>
    <!-- OR if you prefer a button -->
    <form action="${viewEmpUrl}" method="get" style="display:inline;">
        <input type="submit" value="View Employees" />
    </form>
</body>
</html>
