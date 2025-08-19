package com.tss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.tss.DBConnection.DBConnection;
import com.tss.model.LeaveRequest;

public class LeaveRequestDao {

	public boolean updateLeaveStatus(int leaveId, String status, String rejectionReason) {
        String sql = "UPDATE leave_requests SET status = ?, rejection_reason = ? WHERE leave_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, rejectionReason);
            pstmt.setInt(3, leaveId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

	public long calculateLeaveDays(int leaveId) {
		String sql = "SELECT start_date, end_date FROM leave_requests WHERE leave_id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, leaveId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				LocalDate start = LocalDate.parse(rs.getString("start_date"));
				LocalDate end = LocalDate.parse(rs.getString("end_date"));
				return ChronoUnit.DAYS.between(start, end) + 1; // Inclusive of start and end dates
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String getStatus(int leaveId) {
        String sql = "SELECT status FROM leave_requests WHERE leave_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, leaveId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

	public int getEmpId(int leaveId) {
		String sql = "SELECT emp_id FROM leave_requests WHERE leave_id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, leaveId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("emp_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public List<LeaveRequest> getRequestsByEmployee(int empId) {
		List<LeaveRequest> requests = new ArrayList<>();
		String sql = "SELECT * FROM leave_requests WHERE emp_id = ? ORDER BY request_date DESC";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, empId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				LeaveRequest request = new LeaveRequest();
				request.setLeaveId(rs.getInt("leave_id"));
				request.setEmpId(rs.getInt("emp_id"));
				request.setStartDate(rs.getString("start_date"));
				request.setEndDate(rs.getString("end_date"));
				request.setReason(rs.getString("reason"));
				request.setStatus(rs.getString("status"));
				request.setRequestDate(rs.getString("request_date"));
				requests.add(request);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return requests;
	}

	public List<LeaveRequest> getAllRequests() {
        List<LeaveRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM leave_requests ORDER BY request_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                LeaveRequest request = new LeaveRequest();
                request.setLeaveId(rs.getInt("leave_id"));
                request.setEmpId(rs.getInt("emp_id"));
                request.setStartDate(rs.getString("start_date"));
                request.setEndDate(rs.getString("end_date"));
                request.setReason(rs.getString("reason"));
                request.setStatus(rs.getString("status"));
                request.setRequestDate(rs.getString("request_date"));
                request.setRejectionReason(rs.getString("rejection_reason"));
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }
	
	
	public List<LeaveRequest> getRequestsByEmpId(int empId) {
        List<LeaveRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM leave_requests WHERE emp_id = ? ORDER BY request_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, empId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                LeaveRequest request = new LeaveRequest();
                request.setLeaveId(rs.getInt("leave_id"));
                request.setEmpId(rs.getInt("emp_id"));
                request.setStartDate(rs.getString("start_date"));
                request.setEndDate(rs.getString("end_date"));
                request.setReason(rs.getString("reason"));
                request.setStatus(rs.getString("status"));
                request.setRequestDate(rs.getString("request_date"));
                request.setRejectionReason(rs.getString("rejection_reason"));
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

	public boolean saveLeaveRequest(LeaveRequest leaveRequest) {
        String sql = "INSERT INTO leave_requests (emp_id, start_date, end_date, reason, status, request_date, rejection_reason) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, leaveRequest.getEmpId());
            pstmt.setString(2, leaveRequest.getStartDate());
            pstmt.setString(3, leaveRequest.getEndDate());
            pstmt.setString(4, leaveRequest.getReason());
            pstmt.setString(5, leaveRequest.getStatus());
            pstmt.setString(6, leaveRequest.getRequestDate());
            pstmt.setString(7, leaveRequest.getRejectionReason()); // Default to null for new requests
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error saving leave request: " + e.getMessage());
            return false;
        }
    }

}