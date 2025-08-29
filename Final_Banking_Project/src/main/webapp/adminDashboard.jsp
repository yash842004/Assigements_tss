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

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">

<style>
    :root {
        --primary-color: #6c63ff;
        --dark-bg: #121212;
        --content-bg: #1e1e1e;
        --text-color: #e0e0e0;
        --muted-color: #a0a0a0; /* UPDATED: Brighter grey for better visibility */
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
        text-align: center;
    }

    .sidebar .nav-link {
        color: var(--muted-color);
        font-size: 1rem;
        padding: 0.75rem 1rem;
        border-radius: 8px;
        transition: background-color 0.3s, color 0.3s;
    }

    .sidebar .nav-link i { margin-right: 1rem; }
    .sidebar .nav-link.active, .sidebar .nav-link:hover {
        background-color: var(--primary-color);
        color: #fff;
    }

    .sidebar .logout-link { margin-top: auto; }

    /* --- Main Content --- */
    .main-content {
        flex-grow: 1;
        padding: 2rem;
        overflow-y: auto;
    }
    
    .main-header {
        border-bottom: 1px solid var(--border-color);
        padding-bottom: 1.5rem;
        margin-bottom: 2rem;
    }

    .main-content h1 {
        font-weight: 700;
        color: #fff;
    }

    .dashboard-card {
        background-color: var(--content-bg);
        padding: 2rem;
        border-radius: 15px;
        border: 1px solid var(--border-color);
        margin-bottom: 2rem;
        transition: box-shadow 0.3s ease;
    }
    
    .dashboard-card:hover {
        box-shadow: 0 8px 30px rgba(0, 0, 0, 0.4);
    }
    
    .dashboard-card h3 {
        display: flex;
        align-items: center;
        gap: 0.75rem;
        font-weight: 600;
    }

    /* --- Table Styles --- */
    .table {
        color: var(--text-color);
        border-color: var(--border-color);
        margin-top: 1.5rem;
    }
    .table th { color: var(--muted-color); font-weight: 500; border-bottom-width: 2px; }
    .table td { vertical-align: middle; }
    .table-dark {
        --bs-table-bg: var(--content-bg);
        --bs-table-border-color: var(--border-color);
        --bs-table-hover-bg: #2a2a2a;
    }
    
    .no-requests-message td {
        padding: 2rem;
        font-style: italic;
        color: #b5b5b5; 
        font-size: 0.95rem;
    }

    /* --- Form Controls --- */
    .form-control::placeholder {
        color: var(--muted-color);
        opacity: 0.8;
    }

    .btn-outline-secondary {
        --bs-btn-color: var(--muted-color);
        --bs-btn-border-color: var(--border-color);
        --bs-btn-hover-color: #fff;
        --bs-btn-hover-bg: #495057;
        --bs-btn-hover-border-color: #495057;
    }

    /* --- Status & Actions --- */
    .status-badge {
        padding: 0.3em 0.8em;
        border-radius: 50px;
        font-weight: 500;
        font-size: 0.8rem;
    }
    .status-pending { background-color: rgba(255, 193, 7, 0.15); color: var(--warning-color); }
    .status-approved { background-color: rgba(40, 167, 69, 0.15); color: var(--success-color); }
    .status-rejected, .status-deactivated { background-color: rgba(220, 53, 69, 0.15); color: var(--danger-color); }

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

    /* --- Filter Tabs --- */
    .nav-pills .nav-link {
        background-color: var(--content-bg);
        color: var(--muted-color);
        border: 1px solid var(--border-color);
        margin: 0 5px;
        transition: all 0.3s ease;
    }
    .nav-pills .nav-link.active, .nav-pills .nav-link:hover {
        background-color: var(--primary-color);
        color: #fff;
        border-color: var(--primary-color);
    }
    
    /* Animation for tab panes */
    .tab-pane {
        animation: fadeIn 0.5s ease-in-out;
    }
    @keyframes fadeIn {
        from { opacity: 0; transform: translateY(10px); }
        to { opacity: 1; transform: translateY(0); }
    }

    /* --- Responsive --- */
    @media (max-width: 992px) {
        body { flex-direction: column; }
        .sidebar { width: 100%; height: auto; flex-direction: row; justify-content: space-between; align-items: center; padding: 1rem; border-right: none; border-bottom: 1px solid var(--border-color); }
        .sidebar .nav { flex-direction: row; }
        .sidebar .nav-link span { display: none; }
        .sidebar .nav-link i { margin-right: 0; }
        .sidebar .logo, .sidebar .logout-link { margin: 0; }
    }
</style>
</head>
<body>

<div class="sidebar">
    <a href="#" class="logo">AuraBank</a>
    <ul class="nav">
        <li class="nav-item"><a class="nav-link active" href="#"><i class="bi bi-people-fill"></i> <span>Customers</span></a></li>
        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/adminAnalytics"><i class="bi bi-bar-chart-line-fill"></i> <span>Analytics</span></a></li>
    </ul>
    <div class="logout-link">
        <a class="nav-link" href="adminLogout"><i class="bi bi-box-arrow-left"></i> <span>Logout</span></a>
    </div>
</div>

<div class="main-content">
    <div class="main-header">
        <h1>Customer Account Management</h1>
        <p class="text-muted">Search, view, and manage all customer accounts.</p>
    </div>

    <form method="get" action="${pageContext.request.contextPath}/adminDashboard" class="mb-4">
        <div class="input-group">
            <input type="text" name="accountNumber" class="form-control" placeholder="Search by Account Number..." value="${param.accountNumber}" style="background-color: var(--content-bg); border-color: var(--border-color); color: var(--text-color);">
            <button type="submit" class="btn btn-primary"><i class="bi bi-search"></i> Search</button>
            <a href="${pageContext.request.contextPath}/adminDashboard" class="btn btn-outline-secondary">Reset</a>
        </div>
    </form>

    <ul class="nav nav-pills mb-4" id="statusTab" role="tablist">
        <li class="nav-item" role="presentation">
            <button class="nav-link active" id="all-tab" data-bs-toggle="pill" data-bs-target="#all-pane" type="button" role="tab" aria-controls="all-pane" aria-selected="true">
                <i class="bi bi-collection-fill"></i> All Accounts
            </button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link" id="approved-tab" data-bs-toggle="pill" data-bs-target="#approved-pane" type="button" role="tab" aria-controls="approved-pane" aria-selected="false">
                <i class="bi bi-check-circle-fill"></i> Approved
            </button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link" id="pending-tab" data-bs-toggle="pill" data-bs-target="#pending-pane" type="button" role="tab" aria-controls="pending-pane" aria-selected="false">
                <i class="bi bi-clock-history"></i> Pending
            </button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link" id="rejected-tab" data-bs-toggle="pill" data-bs-target="#rejected-pane" type="button" role="tab" aria-controls="rejected-pane" aria-selected="false">
                <i class="bi bi-x-circle-fill"></i> Rejected & Deactivated
            </button>
        </li>
    </ul>

    <div class="tab-content" id="statusTabContent">

        <div class="tab-pane fade show active" id="all-pane" role="tabpanel" aria-labelledby="all-tab" tabindex="0">
            <div class="dashboard-card">
                <h3 class="mb-3 text-success"><i class="bi bi-patch-check-fill"></i> Approved Accounts</h3>
                <div class="table-responsive">
                    <table class="table table-dark table-hover align-middle">
                        <thead>
                            <tr>
                                <th>ID</th><th>Full Name</th><th>Account No.</th><th>Type</th><th>Balance</th><th>Status</th><th class="text-center">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="foundApproved" value="${false}" />
                            <c:forEach var="custView" items="${customers}">
                                <c:if test="${custView.status == 'APPROVED'}">
                                    <c:set var="foundApproved" value="${true}" />
                                    <tr>
                                        <td>${custView.customerId}</td>
                                        <td>${custView.fullName}</td>
                                        <td>${custView.accountNumber != null ? custView.accountNumber : 'N/A'}</td>
                                        <td>${custView.accountType != null ? custView.accountType : 'N/A'}</td>
                                        <td>
                                            <c:if test="${custView.balance != null}"><fmt:formatNumber value="${custView.balance}" type="currency" currencySymbol="$" /></c:if>
                                            <c:if test="${custView.balance == null}">N/A</c:if>
                                        </td>
                                        <td><span class="status-badge status-approved">${custView.status}</span></td>
                                        <td class="text-center">
                                            <a href="deactivateCustomer?id=${custView.customerId}" class="action-btn deactivate" title="Deactivate"><i class="bi bi-slash-circle-fill fs-5"></i></a>
                                            <a href="viewTransactions?id=${custView.customerId}" class="action-btn history" title="History"><i class="bi bi-clock-history fs-5"></i></a>
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                            <c:if test="${not foundApproved}">
                                <tr class="no-requests-message">
                                    <td colspan="7" class="text-center">
                                        <i class="bi bi-check2-circle me-1"></i> No approved accounts found.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
            
            <div class="dashboard-card">
                <h3 class="mb-3 text-warning"><i class="bi bi-hourglass-split"></i> Pending Accounts</h3>
                <div class="table-responsive">
                    <table class="table table-dark table-hover align-middle">
                        <thead>
                            <tr>
                                <th>ID</th><th>Full Name</th><th>Status</th><th class="text-center">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="foundPending" value="${false}" />
                            <c:forEach var="custView" items="${customers}">
                                <c:if test="${custView.status == 'PENDING'}">
                                    <c:set var="foundPending" value="${true}" />
                                    <tr>
                                        <td>${custView.customerId}</td>
                                        <td>${custView.fullName}</td>
                                        <td><span class="status-badge status-pending">${custView.status}</span></td>
                                        <td class="text-center">
                                            <a href="approveCustomer?id=${custView.customerId}" class="action-btn approve" title="Approve"><i class="bi bi-check-circle-fill fs-5"></i></a>
                                            <a href="rejectCustomer?id=${custView.customerId}" class="action-btn reject" title="Reject"><i class="bi bi-x-circle-fill fs-5"></i></a>
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                            <c:if test="${not foundPending}">
                                <tr class="no-requests-message">
                                    <td colspan="4" class="text-center">
                                        <i class="bi bi-check2-all me-1"></i> No pending requests at the moment.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="dashboard-card">
                 <h3 class="mb-3 text-danger"><i class="bi bi-exclamation-triangle-fill"></i> Rejected & Deactivated Accounts</h3>
                <div class="table-responsive">
                    <table class="table table-dark table-hover align-middle">
                        <thead>
                            <tr>
                                <th>ID</th><th>Full Name</th><th>Account No.</th><th>Status</th><th class="text-center">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="foundRejected" value="${false}" />
                            <c:forEach var="custView" items="${customers}">
                                <c:if test="${custView.status == 'REJECTED' || custView.status == 'DEACTIVATED'}">
                                    <c:set var="foundRejected" value="${true}" />
                                    <tr>
                                        <td>${custView.customerId}</td>
                                        <td>${custView.fullName}</td>
                                        <td>${custView.accountNumber != null ? custView.accountNumber : 'N/A'}</td>
                                        <td>
                                            <span class="status-badge ${custView.status == 'REJECTED' ? 'status-rejected' : 'status-deactivated'}">${custView.status}</span>
                                        </td>
                                        <td class="text-center">
                                            <c:if test="${custView.status == 'DEACTIVATED'}">
                                                <a href="activateCustomer?id=${custView.customerId}" class="action-btn activate" title="Activate"><i class="bi bi-arrow-repeat fs-5"></i></a>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                             <c:if test="${not foundRejected}">
                                <tr class="no-requests-message">
                                    <td colspan="5" class="text-center">
                                        <i class="bi bi-info-circle me-1"></i> No rejected or deactivated accounts found.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="tab-pane fade" id="approved-pane" role="tabpanel" aria-labelledby="approved-tab" tabindex="0">
            <div class="dashboard-card">
                <h3 class="mb-3 text-success"><i class="bi bi-patch-check-fill"></i> Approved Accounts</h3>
                <div class="table-responsive">
                    <table class="table table-dark table-hover align-middle">
                        <thead>
                            <tr>
                                <th>ID</th><th>Full Name</th><th>Account No.</th><th>Type</th><th>Balance</th><th>Status</th><th class="text-center">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="foundApproved" value="${false}" />
                            <c:forEach var="custView" items="${customers}">
                                <c:if test="${custView.status == 'APPROVED'}">
                                    <c:set var="foundApproved" value="${true}" />
                                    <tr>
                                        <td>${custView.customerId}</td>
                                        <td>${custView.fullName}</td>
                                        <td>${custView.accountNumber != null ? custView.accountNumber : 'N/A'}</td>
                                        <td>${custView.accountType != null ? custView.accountType : 'N/A'}</td>
                                        <td>
                                            <c:if test="${custView.balance != null}"><fmt:formatNumber value="${custView.balance}" type="currency" currencySymbol="$" /></c:if>
                                            <c:if test="${custView.balance == null}">N/A</c:if>
                                        </td>
                                        <td><span class="status-badge status-approved">${custView.status}</span></td>
                                        <td class="text-center">
                                            <a href="deactivateCustomer?id=${custView.customerId}" class="action-btn deactivate" title="Deactivate"><i class="bi bi-slash-circle-fill fs-5"></i></a>
                                            <a href="viewTransactions?id=${custView.customerId}" class="action-btn history" title="History"><i class="bi bi-clock-history fs-5"></i></a>
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                            <c:if test="${not foundApproved}">
                                <tr class="no-requests-message">
                                    <td colspan="7" class="text-center">
                                        <i class="bi bi-check2-circle me-1"></i> No approved accounts found.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="tab-pane fade" id="pending-pane" role="tabpanel" aria-labelledby="pending-tab" tabindex="0">
             <div class="dashboard-card">
                <h3 class="mb-3 text-warning"><i class="bi bi-hourglass-split"></i> Pending Accounts</h3>
                <div class="table-responsive">
                    <table class="table table-dark table-hover align-middle">
                        <thead>
                            <tr>
                                <th>ID</th><th>Full Name</th><th>Status</th><th class="text-center">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="foundPending" value="${false}" />
                            <c:forEach var="custView" items="${customers}">
                                <c:if test="${custView.status == 'PENDING'}">
                                    <c:set var="foundPending" value="${true}" />
                                    <tr>
                                        <td>${custView.customerId}</td>
                                        <td>${custView.fullName}</td>
                                        <td><span class="status-badge status-pending">${custView.status}</span></td>
                                        <td class="text-center">
                                            <a href="approveCustomer?id=${custView.customerId}" class="action-btn approve" title="Approve"><i class="bi bi-check-circle-fill fs-5"></i></a>
                                            <a href="rejectCustomer?id=${custView.customerId}" class="action-btn reject" title="Reject"><i class="bi bi-x-circle-fill fs-5"></i></a>
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                            <c:if test="${not foundPending}">
                                <tr class="no-requests-message">
                                    <td colspan="4" class="text-center">
                                        <i class="bi bi-check2-all me-1"></i> No pending requests at the moment.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="tab-pane fade" id="rejected-pane" role="tabpanel" aria-labelledby="rejected-tab" tabindex="0">
            <div class="dashboard-card">
                 <h3 class="mb-3 text-danger"><i class="bi bi-exclamation-triangle-fill"></i> Rejected & Deactivated Accounts</h3>
                <div class="table-responsive">
                    <table class="table table-dark table-hover align-middle">
                        <thead>
                            <tr>
                                <th>ID</th><th>Full Name</th><th>Account No.</th><th>Status</th><th class="text-center">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="foundRejected" value="${false}" />
                            <c:forEach var="custView" items="${customers}">
                                <c:if test="${custView.status == 'REJECTED' || custView.status == 'DEACTIVATED'}">
                                    <c:set var="foundRejected" value="${true}" />
                                    <tr>
                                        <td>${custView.customerId}</td>
                                        <td>${custView.fullName}</td>
                                        <td>${custView.accountNumber != null ? custView.accountNumber : 'N/A'}</td>
                                        <td>
                                            <span class="status-badge ${custView.status == 'REJECTED' ? 'status-rejected' : 'status-deactivated'}">${custView.status}</span>
                                        </td>
                                        <td class="text-center">
                                            <c:if test="${custView.status == 'DEACTIVATED'}">
                                                <a href="activateCustomer?id=${custView.customerId}" class="action-btn activate" title="Activate"><i class="bi bi-arrow-repeat fs-5"></i></a>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                            <c:if test="${not foundRejected}">
                                <tr class="no-requests-message">
                                    <td colspan="5" class="text-center">
                                        <i class="bi bi-info-circle me-1"></i> No rejected or deactivated accounts found.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>