package com.tss.service;

import java.time.LocalDate;
import java.util.List;
import com.tss.dao.LeaveRequestDao;
import com.tss.model.LeaveRequest;

public class LeaveRequestService {
	private LeaveRequestDao leaveRequestDao = new LeaveRequestDao();
	private EmployeeService employeeService = new EmployeeService();

	public long countActiveRequests(int empId) {
		List<LeaveRequest> requests = leaveRequestDao.getRequestsByEmpId(empId);
		return requests.stream().filter(r -> "Pending".equals(r.getStatus()) || "Approved".equals(r.getStatus()))
				.count();
	}

	public boolean applyLeave(String startDate, String endDate, String reason) {
		try {
			LocalDate start = LocalDate.parse(startDate);
			LocalDate end = LocalDate.parse(endDate);
			LocalDate today = LocalDate.now();

			System.out.println("applyLeave() -> start=" + start + ", end=" + end + ", today=" + today);

			if (start.isBefore(today)) {
				System.out.println("Leave application failed: start date is before today.");
				return false;
			}

			if (end.isBefore(start)) {
				System.out.println("Leave application failed: end date is before start date.");
				return false;
			}

			if (reason == null || reason.trim().isEmpty()) {
				System.out.println("Leave application failed: reason is empty.");
				return false;
			}

			LeaveRequest leaveRequest = new LeaveRequest();
			leaveRequest.setStartDate(startDate);
			leaveRequest.setEndDate(endDate);
			leaveRequest.setReason(reason.trim());
			leaveRequest.setStatus("Pending");
			leaveRequest.setRequestDate(today.toString());

			boolean saved = leaveRequestDao.saveLeaveRequest(leaveRequest);
			if (!saved) {
				System.out.println("Leave application failed: database save returned false.");
			}
			return saved;

		} catch (Exception e) {
			System.out.println("Leave application failed: exception occurred - " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean approveLeave(int leaveId, int empId) {
		String currentStatus = leaveRequestDao.getStatus(leaveId);
		if (!"Pending".equals(currentStatus)) {
			System.out.println("Cannot approve leaveId " + leaveId + ": Already " + currentStatus);
			return false;
		}
		boolean statusUpdated = leaveRequestDao.updateLeaveStatus(leaveId, "Approved", null);
		System.out.println("Approve status update for leaveId " + leaveId + ": " + statusUpdated);
		return statusUpdated;
	}

	public boolean rejectLeave(int leaveId, String rejectionReason) {
		String currentStatus = leaveRequestDao.getStatus(leaveId);
		if (!"Pending".equals(currentStatus)) {
			return false;
		}
		boolean statusUpdated = leaveRequestDao.updateLeaveStatus(leaveId, "Rejected", rejectionReason);
		return statusUpdated;
	}

	public long calculateLeaveDays(int leaveId) {
		return leaveRequestDao.calculateLeaveDays(leaveId);
	}

	public int getEmpId(int leaveId) {
		return leaveRequestDao.getEmpId(leaveId);
	}

	public List<LeaveRequest> getAllRequestsByEmployee(int empId) {
		return leaveRequestDao.getRequestsByEmployee(empId);
	}

	public List<LeaveRequest> getAllRequests() {
		return leaveRequestDao.getAllRequests();
	}

	public boolean applyLeave(LeaveRequest leaveRequest) {
		System.out.println("Applying leave for empId: " + leaveRequest.getEmpId());
		return leaveRequestDao.saveLeaveRequest(leaveRequest);
	}
}
