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

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <!-- Google Fonts: Poppins -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" >
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <style>
        :root {
            --primary-color: #6c63ff;
            --dark-bg: #121212;
            --content-bg: #1e1e1e;
            --text-color: #e0e0e0;
            --muted-color: #888;
            --border-color: #333;
            --success-color: #28a745;
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
        .sidebar .nav-link.active,
        .sidebar .nav-link:hover {
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
        
        .main-content h1 {
            font-weight: 700;
            margin-bottom: 2rem;
        }
        
        .dashboard-card {
            background-color: var(--content-bg);
            padding: 2rem;
            border-radius: 15px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.3);
            animation: fadeIn 1s ease-out;
            margin-bottom: 2rem;
        }

        .stat-card {
            background-color: var(--content-bg);
            padding: 1.5rem;
            border-radius: 15px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.3);
            transition: transform 0.3s;
        }
        
        .stat-card:hover {
            transform: translateY(-5px);
        }
        
        .stat-card .icon {
            font-size: 2.5rem;
            color: var(--primary-color);
            margin-bottom: 1rem;
        }
        
        .stat-card .stat-value {
            font-size: 2rem;
            font-weight: 700;
            margin-bottom: 0.5rem;
        }
        
        .stat-card .stat-label {
            color: var(--muted-color);
            font-size: 0.9rem;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }

        /* --- Responsive Design --- */
        @media (max-width: 992px) {
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
            .sidebar .nav-link span { display: none; }
            .sidebar .nav-link i { margin-right: 0; }
            .sidebar .logo { margin-bottom: 0; }
            .sidebar .logout-link { margin-top: 0; }
        }
    </style>
</head>
<body>

    <!-- Sidebar Navigation -->
    <div class="sidebar">
        <div>
            <a href="${pageContext.request.contextPath}/adminDashboard" class="logo">AuraBank</a>
            <ul class="nav flex-column">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/adminDashboard">
                        <i class="bi bi-people-fill"></i>
                        <span>Customers</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/adminAnalytics">
                        <i class="bi bi-bar-chart-line-fill"></i>
                        <span>Analytics</span>
                    </a>
                </li>
            </ul>
        </div>
        <div class="logout-link">
             <a class="nav-link" href="${pageContext.request.contextPath}/adminLogout">
                <i class="bi bi-box-arrow-left"></i>
                <span>Logout</span>
            </a>
        </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <h1>Analytics Dashboard</h1>
        
        <!-- Summary Stats -->
        <div class="row mb-4">
            <div class="col-md-3 mb-3">
                <div class="stat-card text-center">
                    <div class="icon"><i class="bi bi-people"></i></div>
                    <div class="stat-value">${totalCustomers}</div>
                    <div class="stat-label">Total Customers</div>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="stat-card text-center">
                    <div class="icon"><i class="bi bi-credit-card"></i></div>
                    <div class="stat-value">${totalAccounts}</div>
                    <div class="stat-label">Active Accounts</div>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="stat-card text-center">
                    <div class="icon"><i class="bi bi-arrow-down-up"></i></div>
                    <div class="stat-value">${totalTransactions}</div>
                    <div class="stat-label">Total Transactions</div>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="stat-card text-center">
                    <div class="icon"><i class="bi bi-currency-dollar"></i></div>
                    <div class="stat-value">$<fmt:formatNumber value="${totalBalance}" pattern="#,##0.00"/></div>
                    <div class="stat-label">Total Balance</div>
                </div>
            </div>
        </div>
        
        <!-- Transaction Analytics Chart -->
        <div class="dashboard-card">
            <h4 class="mb-4">Transaction Activity (Last 7 Days)</h4>
            <canvas id="transactionChart"></canvas>
        </div>
        
        <!-- Transaction Types Chart -->
        <div class="dashboard-card">
            <h4 class="mb-4">Transaction Types Distribution</h4>
            <canvas id="transactionTypesChart"></canvas>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Chart Initialization -->
    <script>
        // Parse the transaction analytics data from server
        const transactionLabels = [<c:forEach var="label" items="${transactionAnalytics.labels}" varStatus="status">"${label}"<c:if test="${!status.last}">,</c:if></c:forEach>];
        const transactionData = [<c:forEach var="count" items="${transactionAnalytics.data}" varStatus="status">${count}<c:if test="${!status.last}">,</c:if></c:forEach>];
        
        // Parse transaction types data
        const typeLabels = [<c:forEach var="entry" items="${transactionTypes}" varStatus="status">"${entry.key}"<c:if test="${!status.last}">,</c:if></c:forEach>];
        const typeData = [<c:forEach var="entry" items="${transactionTypes}" varStatus="status">${entry.value}<c:if test="${!status.last}">,</c:if></c:forEach>];
        
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
    </script>
</body>
</html>