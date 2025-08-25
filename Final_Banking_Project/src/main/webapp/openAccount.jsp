<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>AuraBank - Open New Account</title>

<!-- Bootstrap 5 CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<!-- Bootstrap Icons -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<!-- Google Fonts: Poppins -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap"
	rel="stylesheet">

<style>
:root {
	--primary-color: #6c63ff;
	--dark-bg: #121212;
	--content-bg: #1e1e1e;
	--text-color: #e0e0e0;
	--muted-color: #888;
	--border-color: #333;
}

body {
	font-family: 'Poppins', sans-serif;
	margin: 0;
	background-color: var(--dark-bg);
	color: var(--text-color);
	display: flex;
	min-height: 100vh;
}

/* --- Sidebar --- */
.sidebar {
	width: 260px;
	background-color: var(--content-bg);
	border-right: 1px solid var(--border-color);
	padding: 1.5rem;
	display: flex;
	flex-direction: column;
	flex-shrink: 0;
}

.sidebar .logo {
	font-size: 1.8rem;
	font-weight: 700;
	color: var(--primary-color);
	text-decoration: none;
	margin-bottom: 2rem;
}

.sidebar .nav-link {
	color: var(--muted-color);
	font-size: 1rem;
	padding: 0.75rem 1rem;
	border-radius: 8px;
	transition: background-color 0.3s, color 0.3s;
}

.sidebar .nav-link i {
	margin-right: 1rem;
}

.sidebar .nav-link.active, .sidebar .nav-link:hover {
	background-color: var(--primary-color);
	color: #fff;
}

.sidebar .logout-link {
	margin-top: auto;
}

/* --- Main Content --- */
.main-content {
	flex-grow: 1;
	padding: 2rem;
	overflow-y: auto;
}

.main-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 2rem;
}

.main-header h1 {
	font-weight: 700;
	margin: 0;
}

.form-section {
	background-color: var(--content-bg);
	padding: 2.5rem;
	border-radius: 15px;
	box-shadow: 0 8px 25px rgba(0, 0, 0, 0.3);
	max-width: 600px;
	margin: auto;
	animation: fadeIn 1s ease-out;
}

@
keyframes fadeIn {from { opacity:0;
	transform: translateY(10px);
}

to {
	opacity: 1;
	transform: translateY(0);
}

}

/* --- Form Styling --- */
.form-label {
	color: var(--muted-color);
	margin-bottom: 0.5rem;
}

.form-select {
	background-color: var(--dark-bg);
	color: var(--text-color);
	border-color: var(--border-color);
	padding: 0.75rem 1rem;
}

.form-select:focus {
	background-color: var(--dark-bg);
	color: var(--text-color);
	border-color: var(--primary-color);
	box-shadow: 0 0 0 0.25rem rgba(108, 99, 255, 0.25);
}

.form-select option {
	background-color: var(--content-bg);
}

.btn-submit {
	background-color: var(--primary-color);
	border-color: var(--primary-color);
	padding: 0.75rem;
	font-weight: 500;
	transition: all 0.3s ease;
}

.btn-submit:hover {
	background-color: #5a50e6;
	border-color: #5a50e6;
	transform: translateY(-2px);
}

.back-link {
	color: var(--text-color);
}

/* --- Responsive Design --- */
@media ( max-width : 992px) {
	body {
		flex-direction: column;
	}
	.sidebar {
		width: 100%;
		height: auto;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		padding: 1rem;
	}
	.sidebar .nav {
		flex-direction: row;
	}
	.sidebar .nav-link span {
		display: none;
	}
	.sidebar .nav-link i {
		margin-right: 0;
	}
	.sidebar .logo {
		margin-bottom: 0;
	}
	.sidebar .logout-link {
		margin-top: 0;
	}
}
</style>
</head>
<body>

	<!-- Sidebar Navigation -->
	<div class="sidebar">
		<div>
			<a href="dashboard" class="logo">AuraBank</a>
			<ul class="nav flex-column">
				<li class="nav-item"><a class="nav-link" href="dashboard">
						<i class="bi bi-grid-1x2-fill"></i> <span>Dashboard</span>
				</a></li>
				<li class="nav-item"><a class="nav-link" href="passbook"> <i
						class="bi bi-journal-text"></i> <span>Passbook</span>
				</a></li>
				<li class="nav-item"><a class="nav-link active"
					href="manageAccounts"> <i class="bi bi-gear-fill"></i> <span>Manage
							Accounts</span>
				</a></li>
			</ul>
		</div>
		<div class="logout-link">
			<a class="nav-link" href="logout"> <i
				class="bi bi-box-arrow-left"></i> <span>Logout</span>
			</a>
		</div>
	</div>

	<!-- Main Content -->
	<div class="main-content">
		<div class="main-header">
			<h1>Open a New Account</h1>
			<a href="dashboard" class="btn btn-outline-secondary back-link">
				<i class="bi bi-arrow-left me-2"></i>Back to Dashboard
			</a>
		</div>

		<div class="form-section">
			<c:if test="${not empty errorMessage}">
				<div class="alert alert-danger">${errorMessage}</div>
			</c:if>
			<c:if test="${not empty successMessage}">
				<div class="alert alert-success">${successMessage}</div>
			</c:if>

			<form action="openAccount" method="post">
				<div class="mb-4">
					<label for="type" class="form-label">Select Account Type</label> <select
						id="type" name="type" class="form-select" required>
						<option value="">-- Choose an account type --</option>
						<option value="SAVINGS">Savings Account</option>
						<option value="CHECKING">Checking Account</option>
					</select>
				</div>
				<div class="d-grid">
					<button type="submit" class="btn btn-primary btn-submit">
						<i class="bi bi-plus-circle me-2"></i>Create Account
					</button>
				</div>
			</form>
		</div>
	</div>

	<!-- Bootstrap 5 JS Bundle -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>