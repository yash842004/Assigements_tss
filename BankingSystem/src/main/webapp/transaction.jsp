<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>Perform Transaction</h1>
    <form action="/customer/transaction" method="post">
        Type: <select name="type">
            <option value="deposit">Deposit</option>
            <option value="withdraw">Withdraw</option>
            <option value="transfer">Transfer</option>
        </select><br>
        Amount: <input type="number" name="amount" step="0.01"><br>
        Description: <input type="text" name="description"><br>
        To Account (for transfer): <input type="text" name="toAccountNumber"><br>
        <input type="submit" value="Submit">
    </form>
    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red"><%= request.getAttribute("error") %></p>
    <% } %>
    <a href="/customer/dashboard">Back</a>
</body>
</html>