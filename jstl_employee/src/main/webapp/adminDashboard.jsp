<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Admin Dashboard - Leave Management</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700&family=Montserrat:wght@600&display=swap" rel="stylesheet">
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
.card {
    transition: transform 0.3s ease, box-shadow 0.3s ease;
    cursor: pointer; /* Make cards clickable */
}
.card:hover {
    transform: translateY(-5px);
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}
.footer {
    background-color: #2E2E2E;
    color: #ffffff;
    padding: 20px 0;
}
.action-btn {
    margin-right: 5px;
}
.modal-content {
    border-radius: 10px;
}
.active-card {
    background-color: #d1c4e9; /* Highlight active card */
    border-color: #9575cd;
}
#requestTable tbody tr {
    display: none; /* Hide all rows initially, show via JS */
}
#requestTable tbody tr.data-row {
    display: table-row; /* Show rows matching filter */
}
</style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">Leave Management</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/adminDashboard">Admin Dashboard</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/logout">Logout</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <section class="py-5">
        <div class="container">
            <h2 class="text-center mb-4">Admin Dashboard</h2>

            <!-- Display success or error message -->
            <c:if test="${not empty message}">
                <div class="alert alert-success text-center">${message}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger text-center">${error}</div>
            </c:if>

            <p class="text-center mb-4">Welcome, ${sessionScope.admin.username}</p>
            <div class="row">
                <div class="col-md-4 mb-4">
                    <div class="card h-100 filter-card" data-status="Pending">
                        <div class="card-body text-center">
                            <h5 class="card-title">Pending Requests</h5>
                            <c:set var="pendingCount" value="0" />
                            <c:forEach var="request" items="${requests}">
                                <c:if test="${request.status == 'Pending'}">
                                    <c:set var="pendingCount" value="${pendingCount + 1}" />
                                </c:if>
                            </c:forEach>
                            <p class="card-text">${pendingCount}</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 mb-4">
                    <div class="card h-100 filter-card" data-status="Approved">
                        <div class="card-body text-center">
                            <h5 class="card-title">Approved Leaves</h5>
                            <c:set var="approvedCount" value="0" />
                            <c:forEach var="request" items="${requests}">
                                <c:if test="${request.status == 'Approved'}">
                                    <c:set var="approvedCount" value="${approvedCount + 1}" />
                                </c:if>
                            </c:forEach>
                            <p class="card-text">${approvedCount}</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 mb-4">
                    <div class="card h-100 filter-card" data-status="Rejected">
                        <div class="card-body text-center">
                            <h5 class="card-title">Rejected Leaves</h5>
                            <c:set var="rejectedCount" value="0" />
                            <c:forEach var="request" items="${requests}">
                                <c:if test="${request.status == 'Rejected'}">
                                    <c:set var="rejectedCount" value="${rejectedCount + 1}" />
                                </c:if>
                            </c:forEach>
                            <p class="card-text">${rejectedCount}</p>
                        </div>
                    </div>
                </div>
            </div>
            <!-- History Label -->
            <div class="text-center mb-4">
                <button class="btn btn-secondary filter-card" data-status="All">History</button>
            </div>
            <div class="table-responsive">
                <table class="table table-striped table-hover" id="requestTable">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Employee ID</th>
                            <th>Start Date</th>
                            <th>End Date</th>
                            <th>Reason</th>
                            <th>Status</th>
                            <th>Request Date</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="request" items="${requests}">
                            <tr class="data-row" data-status="${request.status}">
                                <td>${request.leaveId}</td>
                                <td>${request.empId}</td>
                                <td>${request.startDate}</td>
                                <td>${request.endDate}</td>
                                <td>${request.reason}</td>
                                <td>${request.status}</td>
                                <td>${request.requestDate}</td>
                                <td><c:if test="${request.status == 'Pending'}">
                                        <!-- Approve Form -->
                                        <form action="${pageContext.request.contextPath}/approveReject" method="post" style="display:inline;">
                                            <input type="hidden" name="leaveId" value="${request.leaveId}">
                                            <input type="hidden" name="action" value="Approve">
                                            <button type="button" class="btn btn-success action-btn" data-bs-toggle="modal" data-bs-target="#approveModal${request.leaveId}">
                                                Approve
                                            </button>
                                            <!-- Approve Modal -->
                                            <div class="modal fade" id="approveModal${request.leaveId}" tabindex="-1" aria-labelledby="approveModalLabel${request.leaveId}" aria-hidden="true">
                                                <div class="modal-dialog">
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title" id="approveModalLabel${request.leaveId}">Confirm Approval</h5>
                                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                        </div>
                                                        <div class="modal-body">Are you sure you want to approve leave ID ${request.leaveId}?</div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                                            <button type="submit" class="btn btn-success">Confirm Approve</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                        <!-- Reject Form -->
                                        <form action="${pageContext.request.contextPath}/approveReject" method="post" style="display:inline;">
                                            <input type="hidden" name="leaveId" value="${request.leaveId}">
                                            <input type="hidden" name="action" value="Reject">
                                            <button type="button" class="btn btn-danger action-btn" data-bs-toggle="modal" data-bs-target="#rejectModal${request.leaveId}">
                                                Reject
                                            </button>
                                            <!-- Reject Modal -->
                                            <div class="modal fade" id="rejectModal${request.leaveId}" tabindex="-1" aria-labelledby="rejectModalLabel${request.leaveId}" aria-hidden="true">
                                                <div class="modal-dialog">
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title" id="rejectModalLabel${request.leaveId}">Confirm Rejection</h5>
                                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                        </div>
                                                        <div class="modal-body">
                                                            <label for="rejectionReason${request.leaveId}" class="form-label">Reason for Rejection:</label>
                                                            <select class="form-select" id="rejectionReason${request.leaveId}" name="rejectionReason" required>
                                                                <option value="">Select a reason</option>
                                                                <option value="Insufficient Leave Balance">Insufficient Leave Balance</option>
                                                                <option value="Unauthorized Leave">Unauthorized Leave</option>
                                                                <option value="Incomplete Documentation">Incomplete Documentation</option>
                                                                <option value="Personal Conflict">Personal Conflict</option>
                                                                <option value="Other">Other</option>
                                                            </select>
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                                            <button type="submit" class="btn btn-danger">Confirm Reject</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                    </c:if><c:if test="${request.status != 'Pending'}">
                                        <span class="text-muted">No actions available</span>
                                    </c:if></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </section>

    <footer class="footer text-center">
        <div class="container">
            <p>&copy; 2025 Leave Management System. All rights reserved.</p>
            <p><a href="#" class="text-white">Contact Us</a> | <a href="#" class="text-white">Privacy Policy</a></p>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Initialize with all requests visible
        document.addEventListener('DOMContentLoaded', function() {
            filterTable('All'); // Show all on load
        });

        // Filter table based on card click
        document.querySelectorAll('.filter-card').forEach(card => {
            card.addEventListener('click', function() {
                // Remove active class from all cards
                document.querySelectorAll('.filter-card').forEach(c => c.classList.remove('active-card'));
                // Add active class to clicked card
                this.classList.add('active-card');
                // Filter table
                filterTable(this.getAttribute('data-status'));
            });
        });

        function filterTable(status) {
            const rows = document.querySelectorAll('#requestTable tbody tr.data-row');
            rows.forEach(row => {
                const rowStatus = row.getAttribute('data-status');
                if (status === 'All' || rowStatus === status) {
                    row.style.display = 'table-row';
                } else {
                    row.style.display = 'none';
                }
            });
        }
    </script>
</body>
</html>