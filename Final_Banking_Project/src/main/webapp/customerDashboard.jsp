<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>AuraBank - Dashboard</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;600;700&display=swap" rel="stylesheet">

<style>
:root {
    --primary: #6c63ff;
    --secondary: #5a52e0;
    --dark-bg: #0f0f14;
    --card-bg: rgba(40, 40, 55, 0.7);
    --text-light: #e4e4e4;
    --muted: #9ca3af;
}

/* Global */
body {
    font-family: 'Poppins', sans-serif;
    background: var(--dark-bg);
    color: var(--text-light);
    display: flex;
    min-height: 100vh;
    overflow: hidden;
}

/* Sidebar */
.sidebar {
    width: 260px;
    background: #1b1b25;
    backdrop-filter: blur(12px);
    padding: 1.5rem;
    display: flex;
    flex-direction: column;
    transition: all 0.3s ease;
    box-shadow: 2px 0 15px rgba(0,0,0,0.3);
}

.sidebar .logo {
    font-size: 1.8rem;
    font-weight: 700;
    color: var(--primary);
    text-decoration: none;
    margin-bottom: 2rem;
    display: block;
}

.sidebar .nav-link {
    color: var(--muted);
    font-size: 1rem;
    padding: 0.75rem 1rem;
    border-radius: 10px;
    transition: all 0.3s;
    display: flex;
    align-items: center;
}

.sidebar .nav-link i {
    margin-right: 0.75rem;
    font-size: 1.2rem;
}

.sidebar .nav-link.active, 
.sidebar .nav-link:hover {
    background: var(--primary);
    color: #fff;
    transform: translateX(5px);
}

/* Main Content */
.main-content {
    flex-grow: 1;
    padding: 2rem;
    overflow-y: auto;
    background: radial-gradient(circle at top left, #1c1c28, #0f0f14);
}

.main-header h1 {
    font-weight: 700;
    margin-bottom: 2rem;
}

/* Dashboard Sections */
.dashboard-section {
    background: var(--card-bg);
    border-radius: 20px;
    padding: 2rem;
    margin-bottom: 2rem;
    backdrop-filter: blur(12px);
    box-shadow: 0 8px 25px rgba(0,0,0,0.4);
    animation: fadeIn 0.8s ease-out;
}

@keyframes fadeIn {
    from {opacity:0; transform: translateY(10px);}
    to {opacity:1; transform: translateY(0);}
}

/* Account Cards */
.account-card {
    background: linear-gradient(135deg, #2d2d3a, #22222d);
    border: none;
    padding: 1.5rem;
    border-radius: 15px;
    margin-bottom: 1rem;
    transition: all 0.3s ease;
}

.account-card:hover {
    transform: translateY(-5px) scale(1.02);
    box-shadow: 0 5px 20px rgba(108,99,255,0.4);
}

.account-card .account-type {
    font-size: 0.95rem;
    color: var(--muted);
}

.account-card .account-number {
    font-size: 1.1rem;
    letter-spacing: 1px;
}

.account-card .account-balance {
    font-size: 1.8rem;
    font-weight: 700;
    color: var(--primary);
}

/* Quick Actions */
.action-card {
    background: #1f1f2d;
    border-radius: 15px;
    padding: 1.5rem;
    text-align: center;
    transition: all 0.3s;
    cursor: pointer;
    color: var(--text-light);
    display: block;
    text-decoration: none;
}

.action-card:hover {
    background: linear-gradient(135deg, var(--primary), var(--secondary));
    transform: translateY(-5px);
    color: #fff;
    text-decoration: none;
}

.action-card i {
    font-size: 2.5rem;
    margin-bottom: 0.75rem;
    color: var(--primary);
    transition: all 0.3s;
    display: inline-block;
}

.action-card:hover i {
    color: #fff;
    transform: scale(1.15);
}

/* Responsive */
@media(max-width: 992px) {
    body { flex-direction: column; }
    .sidebar { width: 100%; flex-direction: row; justify-content: space-between; }
    .sidebar .logo { margin-bottom: 0; }
    .main-content { padding: 1rem; }
}
</style>
</head>
<body>

    <!-- Sidebar -->
    <div class="sidebar">
        <a href="${pageContext.request.contextPath}/dashboard" class="logo">AuraBank</a>
        <ul class="nav flex-column">
            <li><a class="nav-link active" href="${pageContext.request.contextPath}/dashboard"><i class="bi bi-grid-1x2-fill"></i>Dashboard</a></li>
            <li><a class="nav-link" href="${pageContext.request.contextPath}/passbook"><i class="bi bi-journal-text"></i>Passbook</a></li>
            <li><a class="nav-link" href="${pageContext.request.contextPath}/manageAccounts"><i class="bi bi-gear-fill"></i>Manage Accounts</a></li>
        </ul>
        <div class="logout-link mt-auto">
            <a class="nav-link" href="${pageContext.request.contextPath}/logout"><i class="bi bi-box-arrow-left"></i>Logout</a>
        </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <div class="main-header">
            <h1>Welcome, <span class="text-primary">${sessionScope.customer.fullName}</span></h1>
        </div>

        <!-- Alerts -->
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show">
                <i class="bi bi-check-circle me-2"></i>${successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show">
                <i class="bi bi-exclamation-triangle me-2"></i>${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Account Summary -->
        <div class="dashboard-section">
            <h3 class="mb-4">Your Accounts</h3>
            <c:choose>
                <c:when test="${empty allAccounts}">
                    <p><strong>No accounts found.</strong> Open an account to get started.</p>
                </c:when>
                <c:otherwise>
                    <div class="row">
                        <c:forEach var="acc" items="${allAccounts}">
                            <div class="col-md-6">
                                <div class="account-card">
                                    <div class="d-flex justify-content-between">
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
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Quick Actions -->
        <div class="dashboard-section">
            <h3 class="mb-4">Quick Actions</h3>
            <div class="row g-4">
                <div class="col-md-4">
                    <a href="${pageContext.request.contextPath}/deposit" class="action-card">
                        <i class="bi bi-arrow-down-circle"></i>
                        <div>Deposit Money</div>
                    </a>
                </div>
                <div class="col-md-4">
                    <a href="${pageContext.request.contextPath}/withdraw" class="action-card">
                        <i class="bi bi-arrow-up-circle"></i>
                        <div>Withdraw Money</div>
                    </a>
                </div>
                <div class="col-md-4">
                    <a href="${pageContext.request.contextPath}/transfer" class="action-card">
                        <i class="bi bi-send"></i>
                        <div>Transfer Money</div>
                    </a>
                </div>
            </div>
        </div>
    </div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
