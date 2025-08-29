<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Initial Deposit</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-dark text-light">
<div class="container py-5">
    <div class="card bg-secondary text-light">
        <div class="card-header">
            <h5>Initial Deposit</h5>
        </div>
        <div class="card-body">
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>
            <form action="${pageContext.request.contextPath}/deposit" method="post">
                <input type="hidden" name="accountNumber" value="${param.accountNumber}" />
                <div class="mb-3">
                    <label class="form-label">Account Number</label>
                    <input type="text" class="form-control" value="${param.accountNumber}" readonly />
                </div>
                <div class="mb-3">
                    <label class="form-label">Deposit Amount</label>
                    <input type="number" step="0.01" min="1" class="form-control" name="amount" required />
                </div>
                <button type="submit" class="btn btn-success">Deposit</button>
                <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-outline-light ms-2">Cancel</a>
            </form>
        </div>
    </div>
</div>
</body>
</html>
