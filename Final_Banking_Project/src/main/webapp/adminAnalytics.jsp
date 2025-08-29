<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>AuraBank - Analytics Dashboard</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<link
	href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap"
	rel="stylesheet">

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

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

/* Sidebar */
.sidebar {
	width: 260px;
	background-color: var(--content-bg);
	border-right: 1px solid var(--border-color);
	padding: 1.5rem;
	display: flex;
	flex-direction: column;
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

.sidebar .nav-link.active, .sidebar .nav-link:hover {
	background-color: var(--primary-color);
	color: #fff;
}

.sidebar .logout-link {
	margin-top: auto;
}

/* Main Content */
.main-content {
	flex-grow: 1;
	padding: 1.5rem;
	overflow-y: auto;
}

.dashboard-card {
	background-color: var(--content-bg);
	padding: 1.5rem;
	border-radius: 15px;
	box-shadow: 0 8px 25px rgba(0, 0, 0, 0.3);
	margin-bottom: 1.5rem;
}

.stat-card {
	background-color: var(--content-bg);
	padding: 1rem;
	border-radius: 12px;
	text-align: center;
	transition: transform 0.3s;
	height: 100%;
}

.stat-card:hover {
	transform: translateY(-5px);
}

.stat-card .icon {
	font-size: 2rem;
	color: var(--primary-color);
	margin-bottom: .5rem;
}

.stat-card .stat-value {
	font-size: 1.5rem;
	font-weight: 700;
}

.stat-card .stat-label {
	font-size: .85rem;
	color: var(--muted-color);
}

/* Table */
.table {
	color: var(--text-color);
	border-color: var(--border-color);
}

.table th {
	color: var(--muted-color);
}

/* Responsive Sidebar */
@media ( max-width : 992px) {
	body {
		flex-direction: column;
	}
	.sidebar {
		width: 100%;
		flex-direction: row;
		align-items: center;
		justify-content: space-between;
	}
	.sidebar .nav {
		flex-direction: row;
	}
	.sidebar .nav-link span {
		display: none;
	}
	.sidebar .nav-link i {
		margin: 0;
		font-size: 1.3rem;
	}
}
</style>
</head>
<body>

	<!-- Sidebar -->
	<div class="sidebar">
		<div>
			<a href="${pageContext.request.contextPath}/adminDashboard"
				class="logo">AuraBank</a>
			<ul class="nav flex-column flex-lg-column flex-row">
				<li class="nav-item"><a class="nav-link"
					href="${pageContext.request.contextPath}/adminDashboard"> <i
						class="bi bi-people-fill"></i><span> Customers</span>
				</a></li>
				<li class="nav-item"><a class="nav-link active"
					href="${pageContext.request.contextPath}/adminAnalytics"> <i
						class="bi bi-bar-chart-line-fill"></i><span> Analytics</span>
				</a></li>
			</ul>
		</div>
		<div class="logout-link">
			<a class="nav-link"
				href="${pageContext.request.contextPath}/adminLogout"> <i
				class="bi bi-box-arrow-left"></i><span> Logout</span>
			</a>
		</div>
	</div>

	<!-- Main Content -->
	<div class="main-content">
		<h1 class="mb-4">Analytics Dashboard</h1>

		<!-- Alerts -->
		<c:if test="${not empty errorMessage}">
			<div class="alert alert-danger d-flex align-items-center"
				role="alert">
				<i class="bi bi-exclamation-triangle-fill me-2"></i>${errorMessage}
			</div>
		</c:if>

		<!-- Stats -->
		<div class="row g-3 mb-4">
			<div class="col-6 col-md-4 col-lg-2">
				<div class="stat-card">
					<div class="icon">
						<i class="bi bi-people"></i>
					</div>
					<div class="stat-value">${totalCustomers != null ? totalCustomers : 0}</div>
					<div class="stat-label">Customers</div>
				</div>
			</div>
			<div class="col-6 col-md-4 col-lg-2">
				<div class="stat-card">
					<div class="icon">
						<i class="bi bi-credit-card"></i>
					</div>
					<div class="stat-value">${totalAccounts != null ? totalAccounts : 0}</div>
					<div class="stat-label">Accounts</div>
				</div>
			</div>
			<div class="col-6 col-md-4 col-lg-2">
				<div class="stat-card">
					<div class="icon">
						<i class="bi bi-arrow-down-up"></i>
					</div>
					<div class="stat-value">${totalTransactions != null ? totalTransactions : 0}</div>
					<div class="stat-label">Transactions</div>
				</div>
			</div>
			<div class="col-6 col-md-6 col-lg-3">
				<div class="stat-card">
					<div class="icon">
						<i class="bi bi-currency-dollar"></i>
					</div>
					<div class="stat-value">
						$
						<fmt:formatNumber
							value="${totalBalance != null ? totalBalance : 0}"
							pattern="#,##0.00" />
					</div>
					<div class="stat-label">Total Balance</div>
				</div>
			</div>
			<div class="col-6 col-md-6 col-lg-3">
				<div class="stat-card">
					<div class="icon">
						<i class="bi bi-graph-up"></i>
					</div>
					<div class="stat-value">
						$
						<fmt:formatNumber
							value="${averageBalance != null ? averageBalance : 0}"
							pattern="#,##0.00" />
					</div>
					<div class="stat-label">Average Balance</div>
				</div>
			</div>
		</div>

		<!-- Charts -->
		<div class="row g-3">
			<div class="col-lg-6">
				<div class="dashboard-card">
					<h5>Transaction Activity (7 Days)</h5>
					<canvas id="transactionChart"></canvas>
				</div>
			</div>
			<div class="col-lg-6">
				<div class="dashboard-card">
					<h5>Transaction Types</h5>
					<canvas id="transactionTypesChart"></canvas>
				</div>
			</div>
			<div class="col-lg-6">
				<div class="dashboard-card">
					<h5>Account Types</h5>
					<canvas id="accountTypesChart"></canvas>
				</div>
			</div>
			<div class="col-lg-6">
				<div class="dashboard-card">
					<h5>Customer Status</h5>
					<canvas id="customerStatusChart"></canvas>
				</div>
			</div>
		</div>

		<!-- Recent Transactions -->
		<div class="dashboard-card mt-4">
			<h5>Recent Transactions</h5>
			<div class="table-responsive">
				<table class="table table-dark table-hover align-middle">
					<thead>
						<tr>
							<th>ID</th>
							<th>Account</th>
							<th>Type</th>
							<th>Amount</th>
							<th>Date</th>
							<th>Description</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="transaction" items="${recentTransactions}">
							<tr>
								<td>${transaction.transactionId}</td>
								<td>${transaction.accountId}</td>
								<td><span
									class="badge bg-${transaction.transactionType == 'DEPOSIT' ? 'success' : 
                                                         transaction.transactionType == 'WITHDRAWAL' ? 'danger' : 
                                                         transaction.transactionType == 'TRANSFER_IN' ? 'info' : 'warning'}">
										${transaction.transactionType} </span></td>
								<td>$<fmt:formatNumber value="${transaction.amount}"
										pattern="#,##0.00" /></td>
								<td><fmt:formatDate value="${transaction.transactionDate}"
										pattern="MMM dd, yyyy HH:mm" /></td>
								<td>${transaction.description}</td>
							</tr>
						</c:forEach>
						<c:if test="${empty recentTransactions}">
							<tr>
								<td colspan="6" class="text-center text-muted">No
									transactions found</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

	<script>
        // Parse the transaction analytics data from server
        const transactionLabels = [<c:forEach var="label" items="${transactionAnalytics.labels}" varStatus="status">"${label}"<c:if test="${!status.last}">,</c:if></c:forEach>];
        const transactionData = [<c:forEach var="count" items="${transactionAnalytics.data}" varStatus="status">${count}<c:if test="${!status.last}">,</c:if></c:forEach>];
        
        // Parse transaction types data
        const typeLabels = [<c:forEach var="entry" items="${transactionTypes}" varStatus="status">"${entry.key}"<c:if test="${!status.last}">,</c:if></c:forEach>];
        const typeData = [<c:forEach var="entry" items="${transactionTypes}" varStatus="status">${entry.value}<c:if test="${!status.last}">,</c:if></c:forEach>];
        
        // Parse account types data
        const accountTypeLabels = [<c:forEach var="entry" items="${accountTypes}" varStatus="status">"${entry.key}"<c:if test="${!status.last}">,</c:if></c:forEach>];
        const accountTypeData = [<c:forEach var="entry" items="${accountTypes}" varStatus="status">${entry.value}<c:if test="${!status.last}">,</c:if></c:forEach>];
        
        // Parse customer status data
        const customerStatusLabels = [<c:forEach var="entry" items="${customerStatus}" varStatus="status">"${entry.key}"<c:if test="${!status.last}">,</c:if></c:forEach>];
        const customerStatusData = [<c:forEach var="entry" items="${customerStatus}" varStatus="status">${entry.value}<c:if test="${!status.last}">,</c:if></c:forEach>];
        
        // Provide default data if no analytics data is available
        if (transactionLabels.length === 0) {
            transactionLabels.push("No Data");
            transactionData.push(0);
        }
        
        if (typeLabels.length === 0) {
            typeLabels.push("No Transactions");
            typeData.push(1);
        }
        
        if (accountTypeLabels.length === 0) {
            accountTypeLabels.push("No Accounts");
            accountTypeData.push(1);
        }
        
        if (customerStatusLabels.length === 0) {
            customerStatusLabels.push("No Customers");
            customerStatusData.push(1);
        }
        
        // Transaction Activity Chart
        const transactionCtx = document.getElementById('transactionChart').getContext('2d');
        const transactionChart = new Chart(transactionCtx, {
            type: 'line',
            data: {
                labels: transactionLabels,
                datasets: [{
                    label: 'Number of Transactions',
                    data: transactionData,
                    backgroundColor: 'rgba(108, 99, 255, 0.2)',
                    borderColor: 'rgba(108, 99, 255, 1)',
                    borderWidth: 2,
                    tension: 0.3,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                        labels: {
                            color: '#e0e0e0'
                        }
                    },
                    tooltip: {
                        mode: 'index',
                        intersect: false,
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            color: '#888'
                        },
                        grid: {
                            color: 'rgba(255, 255, 255, 0.1)'
                        }
                    },
                    x: {
                        ticks: {
                            color: '#888'
                        },
                        grid: {
                            color: 'rgba(255, 255, 255, 0.1)'
                        }
                    }
                }
            }
        });
        
        // Transaction Types Chart
        const typesCtx = document.getElementById('transactionTypesChart').getContext('2d');
        const typesChart = new Chart(typesCtx, {
            type: 'doughnut',
            data: {
                labels: typeLabels,
                datasets: [{
                    data: typeData,
                    backgroundColor: [
                        'rgba(108, 99, 255, 0.7)',
                        'rgba(40, 167, 69, 0.7)',
                        'rgba(220, 53, 69, 0.7)',
                        'rgba(255, 193, 7, 0.7)',
                        'rgba(23, 162, 184, 0.7)'
                    ],
                    borderColor: [
                        'rgba(108, 99, 255, 1)',
                        'rgba(40, 167, 69, 1)',
                        'rgba(220, 53, 69, 1)',
                        'rgba(255, 193, 7, 1)',
                        'rgba(23, 162, 184, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'right',
                        labels: {
                            color: '#e0e0e0'
                        }
                    }
                }
            }
        });
        
        // Account Types Chart
        const accountTypesCtx = document.getElementById('accountTypesChart').getContext('2d');
        const accountTypesChart = new Chart(accountTypesCtx, {
            type: 'pie',
            data: {
                labels: accountTypeLabels,
                datasets: [{
                    data: accountTypeData,
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.7)',
                        'rgba(54, 162, 235, 0.7)',
                        'rgba(255, 205, 86, 0.7)',
                        'rgba(75, 192, 192, 0.7)',
                        'rgba(153, 102, 255, 0.7)'
                    ],
                    borderColor: [
                        'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 205, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)'
                    ],
                    borderWidth: 2
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            color: '#e0e0e0'
                        }
                    }
                }
            }
        });
        
        // Customer Status Chart
        const customerStatusCtx = document.getElementById('customerStatusChart').getContext('2d');
        const customerStatusChart = new Chart(customerStatusCtx, {
            type: 'bar',
            data: {
                labels: customerStatusLabels,
                datasets: [{
                    label: 'Number of Customers',
                    data: customerStatusData,
                    backgroundColor: [
                        'rgba(40, 167, 69, 0.7)',
                        'rgba(255, 193, 7, 0.7)',
                        'rgba(220, 53, 69, 0.7)',
                        'rgba(108, 99, 255, 0.7)'
                    ],
                    borderColor: [
                        'rgba(40, 167, 69, 1)',
                        'rgba(255, 193, 7, 1)',
                        'rgba(220, 53, 69, 1)',
                        'rgba(108, 99, 255, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        display: false
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            color: '#888'
                        },
                        grid: {
                            color: 'rgba(255, 255, 255, 0.1)'
                        }
                    },
                    x: {
                        ticks: {
                            color: '#888'
                        },
                        grid: {
                            color: 'rgba(255, 255, 255, 0.1)'
                        }
                    }
                }
            }
        });
    </script>
</body>
</html>