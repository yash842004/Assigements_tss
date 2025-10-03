package com.tss.banking.service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import com.tss.banking.dto.request.FDClosureRequestDTO;
import com.tss.banking.dto.request.FDCreateRequestDTO;
import com.tss.banking.dto.response.FDResponseDTO;
import com.tss.banking.dto.response.PagedResponseDTO;
public interface FixedDepositService {
    FDResponseDTO createFD(FDCreateRequestDTO request);
    FDResponseDTO getFDById(Long fdId);
    FDResponseDTO getFDByFDNumber(String fdNumber);
    PagedResponseDTO<FDResponseDTO> getFDsByCustomerId(Long customerId, int page, int size, String sortBy, String sortDirection);
    PagedResponseDTO<FDResponseDTO> getAllFDs(int page, int size, String sortBy, String sortDirection);
    FDResponseDTO closeFD(FDClosureRequestDTO request);
    FDResponseDTO renewFD(Long fdId);
    List<FDResponseDTO> getMaturedFDs();
    List<FDResponseDTO> getFDsMaturingInDays(int days);
    BigDecimal calculateMaturityAmount(BigDecimal principalAmount, BigDecimal interestRate, int tenureMonths);
    BigDecimal calculateInterestEarned(Long fdId);
    void processAutoRenewals();
    void markMaturedFDs();
    PagedResponseDTO<FDResponseDTO> getFDsByStatus(String status, int page, int size, String sortBy, String sortDirection);
    BigDecimal getTotalActiveFDAmount();
    Long getActiveFDCount();
}
