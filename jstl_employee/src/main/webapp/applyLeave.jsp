<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Apply Leave - Leave Management</title>
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

.btn-primary {
	background-color: #C3B1E1;
	border-color: #C3B1E1;
	transition: background-color 0.3s ease;
}

.btn-primary:hover {
	background-color: #B19CD7;
	border-color: #B19CD7;
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
						href="${pageContext.request.contextPath}/employeeDashboard">Dashboard</a></li>
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/applyLeave">Apply
							Leave</a></li>
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/logout">Logout</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<section class="py-5">
		<div class="container">
			<h2 class="text-center mb-4">Apply for Leave</h2>
			<div class="row justify-content-center">
				<div class="col-md-6">
					<form action="${pageContext.request.contextPath}/applyLeave"
						method="post">
						<div class="mb-3">
							<label for="startDate" class="form-label">Start Date</label> <input
								type="date" class="form-control" id="startDate" name="startDate"
								required>
						</div>
						<div class="mb-3">
							<label for="endDate" class="form-label">End Date</label> <input
								type="date" class="form-control" id="endDate" name="endDate"
								required>
						</div>
						<div class="mb-3">
							<label for="reason" class="form-label">Reason</label>
							<textarea class="form-control" id="reason" name="reason" rows="3"
								required></textarea>
						</div>
						<button type="submit" class="btn btn-primary w-100">Submit</button>
					</form>
					<c:if test="${not empty error}">
						<p style="color: red; text-align: center; margin-top: 10px">${error}</p>
					</c:if>
				</div>
			</div>
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
	<script>
		document.querySelector('form').addEventListener(
				'submit',
				function(e) {
					const startDate = new Date(document
							.getElementById('startDate').value);
					const endDate = new Date(
							document.getElementById('endDate').value);
					const today = new Date();
					today.setHours(0, 0, 0, 0);
					if (startDate < today) {
						alert('Start date cannot be in the past.');
						e.preventDefault();
						return;
					}
					if (endDate <= startDate) {
						alert('End date must be after start date.');
						e.preventDefault();
						return;
					}
				});
	</script>
</body>
</html>