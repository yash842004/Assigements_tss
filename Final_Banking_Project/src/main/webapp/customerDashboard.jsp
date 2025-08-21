<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>AuraBank - Customer Dashboard</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
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
	--success-color: #28a745;
	--danger-color: #dc3545;
}

body {
	font-family: 'Poppins', sans-serif;
	margin: 0;
	background-color: var(--dark-bg);
	color: var(--text-color);
	display: flex;
	min-height: 100vh;
}

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

.main-content {
	flex-grow: 1;
	padding: 2rem;
	overflow-y: auto;
}

.main-header h1 {
	font-weight: 700;
	margin-bottom: 2rem;
}

.dashboard-section {
	background-color: var(--content-bg);
	padding: 2rem;
	border-radius: 15px;
	box-shadow: 0 8px 25px rgba(0, 0, 0, 0.3);
	margin-bottom: 2rem;
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

.account-card {
	background: linear-gradient(135deg, #2a2a2e, #212124);
	border: 1px solid var(--border-color);
	padding: 1.5rem;
	border-radius: 10px;
	margin-bottom: 1rem;
	transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.account-card:hover {
	transform: translateY(-5px);
	box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
}

.account-card .account-type {
	font-size: 1rem;
	font-weight: 500;
	color: var(--muted-color);
}

.account-card .account-number {
	font-size: 1.2rem;
	letter-spacing: 1px;
	color: var(--text-color);
}

.account-card .account-balance {
	font-size: 2rem;
	font-weight: 700;
	color: var(--primary-color);
}

.action-card {
	background-color: #2a2a2e;
	border: 1px solid var(--border-color);
	padding: 1.5rem;
	border-radius: 10px;
	text-align: center;
	transition: background-color 0.3s ease, transform 0.3s ease;
}

.action-card:hover {
	background-color: var(--primary-color);
	transform: translateY(-5px);
}

.action-card a {
	text-decoration: none;
	color: var(--text-color);
	font-weight: 500;
}

.action-card:hover a {
	color: #fff;
}

.action-card i {
	font-size: 2.5rem;
	display: block;
	margin-bottom: 0.5rem;
	color: var(--primary-color);
	transition: color 0.3s ease;
}

.action-card:hover i {
	color: #fff;
}

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

	<div class="sidebar">
		<div>
			<a href="#" class="logo">AuraBank</a>
			<ul class="nav flex-column">
				<li class="nav-item"><a class="nav-link active" href="#"> <i
						class="bi bi-grid-1x2-fill"></i> <span>Dashboard</span>
				</a></li>
				<li class="nav-item"><a class="nav-link" href="passbook"> <i
						class="bi bi-journal-text"></i> <span>Passbook</span>
				</a></li>
				<li class="nav-item"><a class="nav-link" href="manageAccounts">
						<i class="bi bi-gear-fill"></i> <span>Manage Accounts</span>
				</a></li>
			</ul>
		</div>
		<div class="logout-link">
			<a class="nav-link" href="logout"> <i
				class="bi bi-box-arrow-left"></i> <span>Logout</span>
			</a>
		</div>
	</div>

	<div class="main-content">
		<div class="main-header">
			<h1>
				Welcome, <span class="text-primary">${sessionScope.customer.fullName}</span>
			</h1>
		</div>

		<div class="dashboard-section">
			<h3 class="mb-4">Your Account Summary</h3>
			<c:choose>
				<c:when test="${empty allAccounts}">
					<p>
						<strong>No accounts found.</strong> Please open an account to get
						started.
					</p>
				</c:when>
				<c:otherwise>
					<c:forEach var="acc" items="${allAccounts}">
						<div class="account-card">
							<div class="d-flex justify-content-between align-items-start">
								<div>
									<p class="account-type mb-1">${acc.accountType}</p>
									<p class="account-number mb-0">${acc.accountNumber}</p>
								</div>
								<p class="account-balance">
									<fmt:setLocale value="en_US" />
									<fmt:formatNumber value="${acc.balance}" type="currency" />
								</p>
							</div>
						</div>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</div>

		<div class="dashboard-section">
			<h3 class="mb-4">Quick Actions</h3>
			<div class="row g-3">
				<div class="col-md-4">
					<div class="action-card">
						<a href="deposit"> <i class="bi bi-arrow-down-circle"></i>
							Deposit Money
						</a>
					</div>
				</div>
				<div class="col-md-4">
					<div class="action-card">
						<a href="withdraw"> <i class="bi bi-arrow-up-circle"></i>
							Withdraw Money
						</a>
					</div>
				</div>
				<div class="col-md-4">
					<div class="action-card">
						<a href="transfer"> <i class="bi bi-send"></i> Transfer Money
						</a>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
