<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Passbook</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; }
        .container { background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        h1 { color: #333; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 12px; border: 1px solid #ddd; text-align: left; }
        th { background-color: #0056b3; color: white; }
        tr:nth-child(even) { background-color: #f2f2f2; }
        .credit { color: green; }
        .debit { color: red; }
        .back-link { display: inline-block; margin-top: 20px; color: #0056b3; text-decoration: none; }
    </style>
</head>
<body>
    <div class="container">
        <h1>Transaction History (Passbook)</h1>
        <table>
            <thead>
                <tr>
                    <th>Date</th>
                    <th>Description</th>
                    <th>Type</th>
                    <th>Amount</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="tx" items="${transactions}">
                    <tr>
                        <td><fmt:formatDate value="${tx.transactionDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td>${tx.description}</td>
                        <td>${tx.transactionType}</td>
                        <td class="${tx.transactionType.contains('IN') || tx.transactionType.contains('DEPOSIT') ? 'credit' : 'debit'}">
                            <fmt:setLocale value="en_US"/>
                            <fmt:formatNumber value="${tx.amount}" type="currency"/>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <a href="dashboard" class="back-link">&larr; Back to Dashboard</a>
    </div>
</body>
</html>
