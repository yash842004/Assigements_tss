<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AuraBank - Admin Dashboard</title>

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <!-- Google Fonts: Poppins -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">

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
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }

        /* --- Table Styling --- */
        .table {
            color: var(--text-color);
            border-color: var(--border-color);
            margin-top: 1.5rem;
        }
        .table th {
            color: var(--muted-color);
            font-weight: 500;
            border-bottom-width: 2px;
        }
        .table td {
            vertical-align: middle;
        }
        .table-dark {
            --bs-table-bg: var(--content-bg);
            --bs-table-border-color: var(--border-color);
            --bs-table-hover-bg: #2a2a2a;
        }

        /* --- Status Badges --- */
        .status-badge {
            padding: 0.3em 0.8em;
            border-radius: 50px;
            font-weight: 500;
            font-size: 0.8rem;
        }
        .status-pending { background-color: rgba(255, 193, 7, 0.1); color: var(--warning-color); }
        .status-approved { background-color: rgba(40, 167, 69, 0.1); color: var(--success-color); }
        .status-rejected, .status-deactivated { background-color: rgba(220, 53, 69, 0.1); color: var(--danger-color); }

        /* --- Action Buttons --- */
        .action-btn {
            color: var(--muted-color);
            text-decoration: none;
            margin: 0 0.3rem;
            transition: color 0.3s;
        }
        .action-btn.approve:hover { color: var(--success-color); }
        .action-btn.reject:hover { color: var(--danger-color); }
        .action-btn.deactivate:hover { color: var(--warning-color); }
        .action-btn.history:hover, .action-btn.activate:hover { color: var(--primary-color); }

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
            <a href="#" class="logo">AuraBank</a>
            <ul class="nav flex-column">
                <li class="nav-item">
                    <a class="nav-link active" href="#">
                        <i class="bi bi-people-fill"></i>
                        <span>Customers</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">
                        <i class="bi bi-bar-chart-line-fill"></i>
                        <span>Analytics</span>
                    </a>
                </li>
            </ul>
        </div>
        <div class="logout-link">
             <a class="nav-link" href="adminLogout">
                <i class="bi bi-box-arrow-left"></i>
                <span>Logout</span>
            </a>
        </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <h1>Customer Account Management</h1>
        
        <div class="dashboard-card">
            <div class="table-responsive">
                <table class="table table-dark table-hover align-middle">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Full Name</th>
                            <th>Account No.</th>
                            <th>Account Type</th>
                            <th>Balance</th>
                            <th>Status</th>
                            <th class="text-center">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="custView" items="${customers}">
                            <tr>
                                <td>${custView.customerId}</td>
                                <td>${custView.fullName}</td>
                                <td>${custView.accountNumber != null ? custView.accountNumber : 'N/A'}</td>
                                <td>${custView.accountType != null ? custView.accountType : 'N/A'}</td>
                                <td>
                                    <c:if test="${custView.balance != null}">
                                        <fmt:formatNumber value="${custView.balance}" type="currency" currencySymbol="$" />
                                    </c:if>
                                    <c:if test="${custView.balance == null}">N/A</c:if>
                                </td>
                                <td>
                                    <span class="status-badge status-${custView.status.toLowerCase()}">${custView.status}</span>
                                </td>
                                <td class="text-center">
                                    <c:if test="${custView.status == 'PENDING'}">
                                        <a href="approveCustomer?id=${custView.customerId}" class="action-btn approve" title="Approve"><i class="bi bi-check-circle-fill fs-5"></i></a>
                                        <a href="rejectCustomer?id=${custView.customerId}" class="action-btn reject" title="Reject"><i class="bi bi-x-circle-fill fs-5"></i></a>
                                    </c:if>
                                    <c:if test="${custView.status == 'APPROVED'}">
                                        <a href="deactivateCustomer?id=${custView.customerId}" class="action-btn deactivate" title="Deactivate"><i class="bi bi-slash-circle-fill fs-5"></i></a>
                                        <a href="viewTransactions?id=${custView.customerId}" class="action-btn history" title="History"><i class="bi bi-clock-history fs-5"></i></a>
                                    </c:if>
                                    <c:if test="${custView.status == 'DEACTIVATED'}">
                                        <a href="activateCustomer?id=${custView.customerId}" class="action-btn activate" title="Activate"><i class="bi bi-arrow-repeat fs-5"></i></a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>