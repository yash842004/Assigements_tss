<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee List</title>
</head>
<body>
    <h1>Employee List</h1>

    <table border="1">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Salary</th>
        </tr>

        <c:forEach var="emp" items="${employeeList}">
            <tr>
                <td><c:out value="${emp.employeeId}"/></td>
                <td><c:out value="${emp.name}"/></td>
                <td><c:out value="${emp.salary}"/></td>
            </tr>
        </c:forEach>
    </table>

    <p><a href="<c:url value='/showEmployees' />">Back</a></p>
</body>
</html>
