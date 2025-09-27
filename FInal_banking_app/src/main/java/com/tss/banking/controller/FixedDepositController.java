package com.tss.banking.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tss.banking.dto.request.FDClosureRequestDTO;
import com.tss.banking.dto.request.FDCreateRequestDTO;
import com.tss.banking.dto.response.ApiResponseDTO;
import com.tss.banking.dto.response.FDResponseDTO;
import com.tss.banking.dto.response.PagedResponseDTO;
import com.tss.banking.service.FixedDepositService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/fixed-deposits")
@RequiredArgsConstructor
@Slf4j
@Validated
public class FixedDepositController {

    private final FixedDepositService fdService;

    /**
     * Create a new Fixed Deposit
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<FDResponseDTO>> createFD(@Valid @RequestBody FDCreateRequestDTO request) {
        log.info("Creating FD for customer: {}", request.getCustomerId());
        
        FDResponseDTO fdResponse = fdService.createFD(request);
        ApiResponseDTO<FDResponseDTO> response = ApiResponseDTO.success("Fixed Deposit created successfully", fdResponse);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get FD by ID
     */
    @GetMapping("/{fdId}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<FDResponseDTO>> getFDById(@PathVariable @Min(1) Long fdId) {
        log.info("Fetching FD with ID: {}", fdId);
        
        FDResponseDTO fdResponse = fdService.getFDById(fdId);
        ApiResponseDTO<FDResponseDTO> response = ApiResponseDTO.success("FD retrieved successfully", fdResponse);

        return ResponseEntity.ok(response);
    }

    /**
     * Get FD by FD Number
     */
    @GetMapping("/fd-number/{fdNumber}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<FDResponseDTO>> getFDByFDNumber(@PathVariable @NotBlank String fdNumber) {
        log.info("Fetching FD with FD Number: {}", fdNumber);
        
        FDResponseDTO fdResponse = fdService.getFDByFDNumber(fdNumber);
        ApiResponseDTO<FDResponseDTO> response = ApiResponseDTO.success("FD retrieved successfully", fdResponse);

        return ResponseEntity.ok(response);
    }

    /**
     * Get FDs by Customer ID with pagination
     */
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<FDResponseDTO>>> getFDsByCustomer(
            @PathVariable @Min(1) Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        log.info("Fetching FDs for customer: {}", customerId);
        
        PagedResponseDTO<FDResponseDTO> pagedResponse = fdService.getFDsByCustomerId(customerId, page, size, sortBy, sortDirection);
        ApiResponseDTO<PagedResponseDTO<FDResponseDTO>> response = ApiResponseDTO.success("Customer FDs retrieved successfully", pagedResponse);

        return ResponseEntity.ok(response);
    }

    /**
     * Get all FDs with pagination (Admin only)
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<FDResponseDTO>>> getAllFDs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        log.info("Fetching all FDs");
        
        PagedResponseDTO<FDResponseDTO> pagedResponse = fdService.getAllFDs(page, size, sortBy, sortDirection);
        ApiResponseDTO<PagedResponseDTO<FDResponseDTO>> response = ApiResponseDTO.success("All FDs retrieved successfully", pagedResponse);

        return ResponseEntity.ok(response);
    }

    /**
     * Close/Withdraw FD
     */
    @PutMapping("/close")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<FDResponseDTO>> closeFD(@Valid @RequestBody FDClosureRequestDTO request) {
        log.info("Closing FD with ID: {}", request.getFdId());
        
        FDResponseDTO fdResponse = fdService.closeFD(request);
        ApiResponseDTO<FDResponseDTO> response = ApiResponseDTO.success("FD closed successfully", fdResponse);

        return ResponseEntity.ok(response);
    }

    /**
     * Renew FD
     */
    @PutMapping("/{fdId}/renew")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<FDResponseDTO>> renewFD(@PathVariable @Min(1) Long fdId) {
        log.info("Renewing FD with ID: {}", fdId);
        
        FDResponseDTO fdResponse = fdService.renewFD(fdId);
        ApiResponseDTO<FDResponseDTO> response = ApiResponseDTO.success("FD renewed successfully", fdResponse);

        return ResponseEntity.ok(response);
    }

    /**
     * Calculate maturity amount
     */
    @GetMapping("/calculate-maturity")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<BigDecimal>> calculateMaturityAmount(
            @RequestParam @Min(1) BigDecimal principalAmount,
            @RequestParam @Min(1) BigDecimal interestRate,
            @RequestParam @Min(6) int tenureMonths) {
        
        log.info("Calculating maturity amount for principal: {}, rate: {}, tenure: {} months", 
                 principalAmount, interestRate, tenureMonths);
        
        BigDecimal maturityAmount = fdService.calculateMaturityAmount(principalAmount, interestRate, tenureMonths);
        ApiResponseDTO<BigDecimal> response = ApiResponseDTO.success("Maturity amount calculated successfully", maturityAmount);

        return ResponseEntity.ok(response);
    }

    /**
     * Calculate interest earned on FD
     */
    @GetMapping("/{fdId}/interest-earned")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<BigDecimal>> calculateInterestEarned(@PathVariable @Min(1) Long fdId) {
        log.info("Calculating interest earned for FD: {}", fdId);
        
        BigDecimal interestEarned = fdService.calculateInterestEarned(fdId);
        ApiResponseDTO<BigDecimal> response = ApiResponseDTO.success("Interest earned calculated successfully", interestEarned);

        return ResponseEntity.ok(response);
    }

    /**
     * Get matured FDs (Admin only)
     */
    @GetMapping("/matured")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<List<FDResponseDTO>>> getMaturedFDs() {
        log.info("Fetching all matured FDs");
        
        List<FDResponseDTO> maturedFDs = fdService.getMaturedFDs();
        ApiResponseDTO<List<FDResponseDTO>> response = ApiResponseDTO.success("Matured FDs retrieved successfully", maturedFDs);

        return ResponseEntity.ok(response);
    }

    /**
     * Get FDs maturing in specified days (Admin only)
     */
    @GetMapping("/maturing-in-days")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<List<FDResponseDTO>>> getFDsMaturingInDays(
            @RequestParam(defaultValue = "30") @Min(1) int days) {
        
        log.info("Fetching FDs maturing in {} days", days);
        
        List<FDResponseDTO> fds = fdService.getFDsMaturingInDays(days);
        ApiResponseDTO<List<FDResponseDTO>> response = ApiResponseDTO.success("FDs maturing in " + days + " days retrieved successfully", fds);

        return ResponseEntity.ok(response);
    }

    /**
     * Get FDs by status (Admin only)
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<FDResponseDTO>>> getFDsByStatus(
            @PathVariable @NotBlank String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        log.info("Fetching FDs with status: {}", status);
        
        PagedResponseDTO<FDResponseDTO> pagedResponse = fdService.getFDsByStatus(status, page, size, sortBy, sortDirection);
        ApiResponseDTO<PagedResponseDTO<FDResponseDTO>> response = ApiResponseDTO.success("FDs with status " + status + " retrieved successfully", pagedResponse);

        return ResponseEntity.ok(response);
    }

    /**
     * Get FD statistics (Admin only)
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<FDStatistics>> getFDStatistics() {
        log.info("Fetching FD statistics");
        
        BigDecimal totalAmount = fdService.getTotalActiveFDAmount();
        Long activeCount = fdService.getActiveFDCount();
        
        FDStatistics statistics = FDStatistics.builder()
                .totalActiveFDAmount(totalAmount)
                .activeFDCount(activeCount)
                .build();
        
        ApiResponseDTO<FDStatistics> response = ApiResponseDTO.success("FD statistics retrieved successfully", statistics);

        return ResponseEntity.ok(response);
    }

    // Inner class for FD statistics
    @lombok.Data
    @lombok.Builder
    public static class FDStatistics {
        private BigDecimal totalActiveFDAmount;
        private Long activeFDCount;
    }
}
