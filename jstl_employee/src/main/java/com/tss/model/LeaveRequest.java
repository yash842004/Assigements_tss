package com.tss.model;

public class LeaveRequest {
    private int leaveId;
    private int empId;
    private String startDate;
    private String endDate;
    private String reason;
    private String status;
    private String requestDate;
    private String rejectionReason; 

    public LeaveRequest() {
    }

    public LeaveRequest(int leaveId, int empId, String startDate, String endDate, String reason, String status, String requestDate, String rejectionReason) {
        this.leaveId = leaveId;
        this.empId = empId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = status;
        this.requestDate = requestDate;
        this.rejectionReason = rejectionReason;
    }

    public int getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(int leaveId) {
        this.leaveId = leaveId;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    @Override
    public String toString() {
        return "LeaveRequest [leaveId=" + leaveId + ", empId=" + empId + ", startDate=" + startDate + ", endDate=" + endDate +
               ", reason=" + reason + ", status=" + status + ", requestDate=" + requestDate + ", rejectionReason=" + rejectionReason + "]";
    }
}