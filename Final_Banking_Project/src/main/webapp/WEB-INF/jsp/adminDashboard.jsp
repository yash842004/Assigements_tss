<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<title>Admin Dashboard</title>
<style>
body {
	font-family: Arial, sans-serif;
	margin: 20px;
	background-color: #f4f4f4;
}

h1, h2 {
	color: #333;
}

.container {
	background-color: #fff;
	padding: 20px;
	border-radius: 8px;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
}

th, td {
	padding: 12px;
	border: 1px solid #ddd;
	text-align: left;
}

th {
	background-color: #0056b3;
	color: white;
}

tr:nth-child(even) {
	background-color: #f2f2f2;
}

a {
	color: #0056b3;
	text-decoration: none;
}

a:hover {
	text-decoration: underline;
}

.logout {
	float: right;
	font-size: 16px;
}

.status-pending {
	color: orange;
	font-weight: bold;
}

.status-approved {
	color: #28a745;
	font-weight: bold;
}

.status-rejected, .status-deactivated {
	color: #dc3545;
	font-weight: bold;
}
</style>
</head>
<body>
	<div class="container">
		<a href="adminLogout" class="logout">Logout</a>
		<h1>Admin Dashboard</h1>
		<h2>Customer Account Management</h2>

		<table>
			<thead>
				<tr>
					<th>ID</th>
					<th>Full Name</th>
					<th>Account No.</th>
					<th>Balance</th>
					<th>Status</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="custView" items="${customers}">
					<tr>
						<td>${custView.customerId}</td>
						<td>${custView.fullName}</td>
						<td>${custView.accountNumber != null ? custView.accountNumber : 'N/A'}</td>
						<td><c:if test="${custView.balance != null}">
								<fmt:formatNumber value="${custView.balance}" type="currency"
									currencySymbol="$" />
							</c:if> <c:if test="${custView.balance == null}">N/A</c:if></td>
						<td><span class="status-${custView.status.toLowerCase()}">${custView.status}</span>
						</td>
						<td><c:if test="${custView.status == 'PENDING'}">
								<a href="approveCustomer?id=${custView.customerId}">Approve</a> |
                                <a
									href="rejectCustomer?id=${custView.customerId}">Reject</a>
							</c:if> <c:if test="${custView.status == 'APPROVED'}">
								<a href="deactivateCustomer?id=${custView.customerId}">Deactivate</a> |
                                <a
									href="viewTransactions?id=${custView.customerId}">History</a>
							</c:if> <c:if test="${custView.status == 'DEACTIVATED'}">
								<a href="activateCustomer?id=${custView.customerId}">Activate</a>
							</c:if></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>