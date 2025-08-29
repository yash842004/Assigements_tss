<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>AuraBank - Manage Accounts</title>

<!-- Bootstrap 5 CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<!-- Bootstrap Icons -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<!-- Google Fonts: Poppins -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" >
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
	--warning-color: #ffc107;
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

/* --- Account Cards --- */
.account-card {
	background-color: #2a2a2e;
	border: 1px solid var(--border-color);
	border-radius: 10px;
	padding: 1.5rem;
	display: flex;
	flex-direction: column;
	height: 100%;
}

.account-card h5 {
	font-weight: 600;
	color: var(--primary-color);
}

.account-info p {
	margin-bottom: 0.5rem;
	color: var(--muted-color);
}

.account-info p strong {
	color: var(--text-color);
}

.account-actions {
	margin-top: auto;
	padding-top: 1rem;
	border-top: 1px solid var(--border-color);
}

.no-accounts {
	text-align: center;
	padding: 3rem;
}

/* --- Modal Styling --- */
.modal-content {
	background-color: var(--content-bg);
	color: var(--text-color);
	border: 1px solid var(--border-color);
}

.modal-header {
	border-bottom-color: var(--border-color);
}

.modal-footer {
	border-top-color: var(--border-color);
}

.btn-close {
	filter: invert(1) grayscale(100%) brightness(200%);
}

.form-select {
	background-color: var(--dark-bg);
	color: var(--text-color);
	border-color: var(--border-color);
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
			<a href="${pageContext.request.contextPath}/dashboard" class="logo">AuraBank</a>
			<ul class="nav flex-column">
				<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/dashboard">
						<i class="bi bi-grid-1x2-fill"></i> <span>Dashboard</span>
				</a></li>
				<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/passbook"> <i
						class="bi bi-journal-text"></i> <span>Passbook</span>
				</a></li>
				<li class="nav-item"><a class="nav-link active"
					href="${pageContext.request.contextPath}/manageAccounts"> <i class="bi bi-gear-fill"></i> <span>Manage
							Accounts</span>
				</a></li>
			</ul>
		</div>
		<div class="logout-link">
			<a class="nav-link" href="${pageContext.request.contextPath}/logout"> <i
				class="bi bi-box-arrow-left"></i> <span>Logout</span>
			</a>
		</div>
	</div>

	<!-- Main Content -->
	<div class="main-content">
		<div class="main-header">
			<h1>Manage Your Accounts</h1>
			<a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-secondary back-link">
				<i class="bi bi-arrow-left me-2"></i>Back to Dashboard
			</a>
		</div>

		<div class="dashboard-section">
			<c:if test="${not empty successMessage}">
				<div class="alert alert-success">${successMessage}</div>
			</c:if>
			<c:if test="${not empty errorMessage}">
				<div class="alert alert-danger">${errorMessage}</div>
			</c:if>

			<c:choose>
				<c:when test="${empty accounts}">
					<div class="no-accounts">
						<h3>No Accounts Found</h3>
						<p class="text-muted">You don't have any accounts yet.</p>
						<a href="${pageContext.request.contextPath}/openAccount" class="btn btn-primary mt-3"> <i
						class="bi bi-plus-circle me-2"></i>Open Your First Account
					</a>
					</div>
				</c:when>
				<c:otherwise>
					<div class="row g-4">
						<c:forEach var="account" items="${accounts}">
							<div class="col-md-6 col-lg-4">
								<div class="account-card">
									<h5>${account.accountType}Account</h5>
									<div class="account-info">
										<p>
											<strong>Number:</strong> ${account.accountNumber}
										</p>
										<p>
											<strong>Balance:</strong>
											<fmt:setLocale value="en_US" />
											<fmt:formatNumber value="${account.balance}" type="currency" />
										</p>
										<p>
											<strong>Created:</strong>
											<fmt:formatDate value="${account.createdDate}"
												pattern="MMM dd, yyyy" />
										</p>
									</div>
									<div class="account-actions d-flex gap-2">
										<button class="btn btn-sm btn-outline-warning w-100"
											data-bs-toggle="modal" data-bs-target="#changeTypeModal"
											data-account-id="${account.accountId}">
											<i class="bi bi-pencil-square me-1"></i> Change Type
										</button>
										<button class="btn btn-sm btn-outline-danger w-100"
											data-bs-toggle="modal" data-bs-target="#deleteModal"
											data-account-id="${account.accountId}"
											data-account-number="${account.accountNumber}">
											<i class="bi bi-trash me-1"></i> Delete
										</button>
									</div>
								</div>
							</div>
						</c:forEach>
					</div>
				</c:otherwise>
			</c:choose>

			<div class="text-center mt-4">
				<a href="${pageContext.request.contextPath}/openAccount" class="btn btn-primary"> <i
					class="bi bi-plus-circle me-2"></i>Open New Account
				</a>
			</div>
		</div>
	</div>

	<!-- Change Account Type Modal -->
	<div class="modal fade" id="changeTypeModal" tabindex="-1"
		aria-labelledby="changeTypeModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="changeTypeModalLabel">Change
						Account Type</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<form id="changeTypeForm" action="${pageContext.request.contextPath}/updateAccountType" method="post">
					<div class="modal-body">
						<input type="hidden" id="changeAccountId" name="accountId">
						<div class="mb-3">
							<label for="newAccountType" class="form-label">New
								Account Type:</label> <select id="newAccountType" name="newAccountType"
								class="form-select" required>
								<option value="">-- Select New Type --</option>
								<option value="SAVINGS">Savings</option>
								<option value="CHECKING">Checking</option>
							</select>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary"
							data-bs-dismiss="modal">Cancel</button>
						<button type="submit" class="btn btn-primary">Update Type</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<!-- Delete Confirmation Modal -->
	<div class="modal fade" id="deleteModal" tabindex="-1"
		aria-labelledby="deleteModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="deleteModalLabel">Confirm Account
						Deletion</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<form id="deleteForm" action="${pageContext.request.contextPath}/deleteAccount" method="post">
					<div class="modal-body">
						<input type="hidden" id="deleteAccountId" name="accountId">
						<p>
							Are you sure you want to delete account <strong
								id="deleteAccountNumber"></strong>?
						</p>
						<p class="text-danger fw-bold">This action cannot be undone!</p>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary"
							data-bs-dismiss="modal">Cancel</button>
						<button type="submit" class="btn btn-danger">Delete
							Account</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<!-- Bootstrap 5 JS Bundle -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
	<script>
		// Script to pass data to modals using Bootstrap 5 event listeners
		const changeTypeModal = document.getElementById('changeTypeModal');
		changeTypeModal.addEventListener('show.bs.modal', function(event) {
			const button = event.relatedTarget;
			const accountId = button.getAttribute('data-account-id');
			const modalAccountIdInput = changeTypeModal
					.querySelector('#changeAccountId');
			modalAccountIdInput.value = accountId;
		});

		const deleteModal = document.getElementById('deleteModal');
		deleteModal.addEventListener('show.bs.modal', function(event) {
			const button = event.relatedTarget;
			const accountId = button.getAttribute('data-account-id');
			const accountNumber = button.getAttribute('data-account-number');

			const modalAccountIdInput = deleteModal
					.querySelector('#deleteAccountId');
			const modalAccountNumberSpan = deleteModal
					.querySelector('#deleteAccountNumber');

			modalAccountIdInput.value = accountId;
			modalAccountNumberSpan.textContent = accountNumber;
		});
	</script>
</body>
</html>