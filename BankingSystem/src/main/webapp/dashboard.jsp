<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head><title>Customer Dashboard</title></head>
<body>
    <h1>Welcome, ${user.fullName}</h1>
    <a href="/customer/account">View Account</a><br>
    <a href="/customer/passbook">View Passbook</a><br>
    <a href="/customer/transaction">Perform Transaction</a><br>
    <a href="/logout">Logout</a>
</body>
</html>