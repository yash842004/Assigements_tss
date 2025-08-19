<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>practice pages</h1>
	<h2>C:set and C:out</h2>
	<c:set var="username" value="raj patel" />
	<p>
		User:
		<c:out value="${username}" />
	</p>

	<h1>c:if</h1>
	<c:set var="age" value="20" />
	<c:if test="${age > 18}">
		<p>you are eligible to riding</p>
	</c:if>



	<h2>c:choose c:when c:otherwise</h2>
	<c:set var="marks" value="99" />
	<c:choose>
		<c:when test="${marks >= 90 }">Grade: A</c:when>
		<c:when test="${marks >= 80 }">Grade: B</c:when>
		<c:otherwise>Grade: C</c:otherwise>
	</c:choose>


	<h1>c:for each</h1>
	<c:forEach var='i' begin='0' end='4'>
		<p>Number: ${i}</p>
	</c:forEach>


	<h1>c: forTokens</h1>
	<c:forTokens var="lang" items="java:c++" delims=':'>
		<p>Language: ${lang}</p>
	</c:forTokens>

	<h1>c:catch</h1>
	<c:catch var="err">
		<%
		int number = 10 / 0;
		%>
	</c:catch>
	<c:if test="${not empty err }">
		<p>Error: ${err }</p>
	</c:if>

	<h1>Functions :</h1>
	<h2>fn:length</h2>
	<p>Length of 'Hello JSTL': ${fn:length('Hello JSTL')}</p>
	<p>ios hiii mac: ${fn:length('hiii mac')}</p>

	<h2>fn:contains</h2>
	<p>${fn:contains('I am programmer','am')}</p>

	<h2>fn:toUpperCase</h2>
	<p>${fn:toUpperCase('java')}</p>
	<h2>fn:toLowerCase</h2>
	<p>${fn:toLowerCase('RAJ')}</p>

	<h2>fn: substring</h2>
	<p>${fn:substring('python',1,4)}</p>
	>

	<h2>Array</h2>
	  <c:set var="names" value="${['yash','raj','om']}" />
    
        <ul>
        <c:forEach var="n" items="${names}">
            <li>${n}</li>
        </c:forEach>
    </ul>



	<h1>fmt : formating</h1>
	<p>
		<fmt:formatNumber value="12345.678" type="currency"
			currencySymbol="Rs: " />
		<fmt:formatNumber value="0.85" type="percent" />
	</p>

	<h1>fmt: date formating</h1>
	<jsp:useBean id="now" class="java.util.Date" />
	<fmt:formatDate value="${now}" pattern="dd-MM-yyyy HH:mm:ss" />
	
	
	
 <h1>URL Tag Example</h1>
    <a href="<c:url value='https://www.google.com/' />">Home</a>

    <h1>Redirect Example</h1>
    <c:redirect url="Welcome.jsp" />

</body>
</html>