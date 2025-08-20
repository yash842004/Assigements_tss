<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AuraBank - Passbook</title>

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
            box-shadow: 0 8px 25px rgba(0,0,0,0.3);
            animation: fadeIn 1s ease-out;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }

        /* --- Form & Table Styling --- */
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

        .credit { color: var(--success-color); font-weight: 500; }
        .debit { color: var(--danger-color); font-weight: 500; }

        .no-data-message {
            text-align: center;
            padding: 3rem;
            color: var(--muted-color);
        }
        .no-data-message i {
            font-size: 3rem;
            display: block;
            margin-bottom: 1rem;
        }
        
        .back-link {
            color: var(--text-color);
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
            <a href="dashboard" class="logo">AuraBank</a>
            <ul class="nav flex-column">
                <li class="nav-item">
                    <a class="nav-link" href="dashboard">
                        <i class="bi bi-grid-1x2-fill"></i>
                        <span>Dashboard</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="passbook">
                        <i class="bi bi-journal-text"></i>
                        <span>Passbook</span>
                    </a>
                </li>
                 <li class="nav-item">
                    <a class="nav-link" href="manageAccounts">
                        <i class="bi bi-gear-fill"></i>
                        <span>Manage Accounts</span>
                    </a>
                </li>
            </ul>
        </div>
        <div class="logout-link">
             <a class="nav-link" href="logout">
                <i class="bi bi-box-arrow-left"></i>
                <span>Logout</span>
            </a>
        </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <div class="main-header">
            <h1>Transaction History</h1>
            <a href="dashboard" class="btn btn-outline-secondary back-link">
                <i class="bi bi-arrow-left me-2"></i>Back to Dashboard
            </a>
        </div>
        
        <div class="dashboard-section">
            <c:choose>
                <c:when test="${empty accounts}">
                    <div class="no-data-message">
                        <i class="bi bi-exclamation-circle"></i>
                        <h3>No Accounts Found</h3>
                        <p>You need to open an account to view transactions.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="account-selector mb-4">
                        <form method="get" action="passbook" class="row align-items-center">
                             <div class="col-auto">
                                <label for="accountId" class="col-form-label">Select Account:</label>
                            </div>
                            <div class="col-md-6 col-lg-5">
                                <select id="accountId" name="accountId" class="form-select" onchange="this.form.submit()">
                                    <c:forEach var="acc" items="${accounts}">
                                        <option value="${acc.accountId}" ${acc.accountId == selectedAccountId ? 'selected' : ''}>
                                            ${acc.accountType} - ${acc.accountNumber} 
                                            (<fmt:formatNumber value="${acc.balance}" type="currency"/>)
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </form>
                    </div>
                    
                    <c:choose>
                        <c:when test="${empty transactions}">
                             <div class="no-data-message">
                                <i class="bi bi-journal-x"></i>
                                <h3>No Transactions Found</h3>
                                <p>There are no transactions for the selected account.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-dark table-hover align-middle">
                                    <thead>
                                        <tr>
                                            <th>Date</th>
                                            <th>Description</th>
                                            <th>Type</th>
                                            <th class="text-end">Amount</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="tx" items="${transactions}">
                                            <tr>
                                                <td><fmt:formatDate value="${tx.transactionDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                                <td>${tx.description}</td>
                                                <td>${tx.transactionType}</td>
                                                <td class="text-end ${fn:contains(tx.transactionType, 'IN') or fn:contains(tx.transactionType, 'DEPOSIT') ? 'credit' : 'debit'}">
                                                    <fmt:setLocale value="en_US"/>
                                                    <c:if test="${fn:contains(tx.transactionType, 'IN') or fn:contains(tx.transactionType, 'DEPOSIT')}">+</c:if>
                                                    <c:if test="${not (fn:contains(tx.transactionType, 'IN') or fn:contains(tx.transactionType, 'DEPOSIT'))}">-</c:if>
                                                    $<fmt:formatNumber value="${tx.amount}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
