<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Admin Dashboard - Leave Management</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700&family=Montserrat:wght@600&display=swap"
	rel="stylesheet">
<style>
body {
	font-family: 'Lato', sans-serif;
	background-color: #FAF7F0;
	color: #2E2E2E;
}

h2 {
	font-family: 'Montserrat', sans-serif;
}

.navbar {
	background: linear-gradient(135deg, #C3B1E1, #BEE3DB);
}

.navbar-brand, .nav-link {
	color: #ffffff !important;
	transition: color 0.3s ease;
}

.nav-link:hover {
	color: #FFD700 !important;
}

.btn-success {
	background-color: #BEE3DB;
	border-color: #BEE3DB;
}

.btn-success:hover {
	background-color: #A8D8CE;
	border-color: #A8D8CE;
}

.btn-danger {
	background-color: #E1B1B1;
	border-color: #E1B1B1;
}

.btn-danger:hover {
	background-color: #D7A3A3;
	border-color: #D7A3A3;
}

.btn:disabled {
	opacity: 0.6;
	cursor: not-allowed;
}

.modal-content {
	border-radius: 10px;
}

.footer {
	background-color: #2E2E2E;
	color: #ffffff;
	padding: 20px 0;
}
</style>
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-dark">
		<div class="container">
			<a class="navbar-brand" href="${pageContext.request.contextPath}/">Leave
				Management</a>
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarNav">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarNav">
				<ul class="navbar-nav ms-auto">
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/adminDashboard">Admin
							Dashboard</a></li>
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/logout">Logout</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<section class="py-5">
		<div class="container">
			<h2 class="text-center mb-4">Admin Dashboard</h2>
			<div class="table-responsive">
				<table class="table table-striped table-hover">
					<thead>
						<tr>
							<th>ID</th>
							<th>Employee ID</th>
							<th>Start Date</th>
							<th>End Date</th>
							<th>Reason</th>
							<th>Status</th>
							<th>Request Date</th>
							<th>Action</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="request" items="${requests}">
							<tr>
								<td>${request.leaveId}</td>
								<td>${request.empId}</td>
								<td>${request.startDate}</td>
								<td>${request.endDate}</td>
								<td>${request.reason}</td>
								<td>${request.status}</td>
								<td>${request.requestDate}</td>
								<td><c:if test="${request.status == 'Pending'}">
										<form
											action="${pageContext.request.contextPath}/approveReject"
											method="post" style="display: inline;">
											<input type="hidden" name="leaveId"
												value="${request.leaveId}">
											<button type="submit" name="action" value="Approve"
												class="btn btn-success btn-sm" data-bs-toggle="modal"
												data-bs-target="#approveModal${request.leaveId}">Approve</button>
										</form>
										<form
											action="${pageContext.request.contextPath}/approveReject"
											method="post" style="display: inline;">
											<input type="hidden" name="leaveId"
												value="${request.leaveId}">
											<button type="submit" name="action" value="Reject"
												class="btn btn-danger btn-sm" data-bs-toggle="modal"
												data-bs-target="#rejectModal${request.leaveId}">Reject</button>
										</form>
									</c:if> <c:if test="${request.status != 'Pending'}">
										<span class="text-muted">Action Locked</span>
									</c:if></td>
							</tr>
							<div class="modal fade" id="approveModal${request.leaveId}"
								tabindex="-1"
								aria-labelledby="approveModalLabel${request.leaveId}"
								aria-hidden="true">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header">
											<h5 class="modal-title"
												id="approveModalLabel${request.leaveId}">Confirm
												Approval</h5>
											<button type="button" class="btn-close"
												data-bs-dismiss="modal" aria-label="Close"></button>
										</div>
										<div class="modal-body">Are you sure you want to approve
											leave request #${request.leaveId}?</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-secondary"
												data-bs-dismiss="modal">Cancel</button>
											<button type="submit" class="btn btn-primary"
												formaction="${pageContext.request.contextPath}/approveReject"
												formmethod="post" name="action" value="Approve"
												form="approveForm${request.leaveId}">Approve</button>
											<input type="hidden" name="leaveId"
												value="${request.leaveId}"
												form="approveForm${request.leaveId}">
										</div>
									</div>
								</div>
							</div>
							<div class="modal fade" id="rejectModal${request.leaveId}"
								tabindex="-1"
								aria-labelledby="rejectModalLabel${request.leaveId}"
								aria-hidden="true">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header">
											<h5 class="modal-title"
												id="rejectModalLabel${request.leaveId}">Confirm
												Rejection</h5>
											<button type="button" class="btn-close"
												data-bs-dismiss="modal" aria-label="Close"></button>
										</div>
										<div class="modal-body">Are you sure you want to reject
											leave request #${request.leaveId}?</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-secondary"
												data-bs-dismiss="modal">Cancel</button>
											<button type="submit" class="btn btn-danger"
												formaction="${pageContext.request.contextPath}/approveReject"
												formmethod="post" name="action" value="Reject"
												form="rejectForm${request.leaveId}">Reject</button>
											<input type="hidden" name="leaveId"
												value="${request.leaveId}"
												form="rejectForm${request.leaveId}">
										</div>
									</div>
								</div>
							</div>
							<form id="approveForm${request.leaveId}" method="post"
								style="display: none;"></form>
							<form id="rejectForm${request.leaveId}" method="post"
								style="display: none;"></form>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<c:if test="${not empty error}">
				<p style="color: red; text-align: center; margin-top: 10px">${error}</p>
			</c:if>
		</div>
	</section>

	<footer class="footer text-center">
		<div class="container">
			<p>&copy; 2025 Leave Management System. All rights reserved.</p>
			<p>
				<a href="#" class="text-white">Contact Us</a> | <a href="#"
					class="text-white">Privacy Policy</a>
			</p>
		</div>
	</footer>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>