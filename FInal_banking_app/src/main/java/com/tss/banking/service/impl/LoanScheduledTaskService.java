package com.tss.banking.service.impl;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.tss.banking.dto.response.LoanResponseDTO;
import com.tss.banking.service.LoanService;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class LoanScheduledTaskService {
    @Autowired
    private LoanService loanService;
    @Scheduled(cron = "0 0 9 * * ?")
    public void processAutomaticEMIDeductions() {
        log.info("Starting automatic EMI deductions for today: {}", LocalDate.now());
        try {
            List<LoanResponseDTO> loansWithDuePayments = loanService.getLoansWithDuePayments();
            for (LoanResponseDTO loan : loansWithDuePayments) {
                try {
                    loanService.processAutomaticEMIDeduction(loan.getId());
                    log.info("Processed automatic EMI for loan: {}", loan.getLoanNumber());
                } catch (Exception e) {
                    log.error("Failed to process automatic EMI for loan: {}", loan.getLoanNumber(), e);
                }
            }
            log.info("Completed automatic EMI deductions. Processed {} loans", loansWithDuePayments.size());
        } catch (Exception e) {
            log.error("Error during automatic EMI deduction process", e);
        }
    }
    @Scheduled(cron = "0 0 10 * * ?")
    public void processOverdueLoans() {
        log.info("Starting overdue loan processing for date: {}", LocalDate.now());
        try {
            loanService.processOverdueLoans();
            log.info("Completed overdue loan processing");
        } catch (Exception e) {
            log.error("Error during overdue loan processing", e);
        }
    }
    @Scheduled(cron = "0 0 8 ? * MON")
    public void generateOverdueLoanReport() {
        log.info("Generating weekly overdue loan report for date: {}", LocalDate.now());
        try {
            List<LoanResponseDTO> overdueLoans = loanService.getOverdueLoans();
            if (!overdueLoans.isEmpty()) {
                log.warn("Found {} overdue loans:", overdueLoans.size());
                for (LoanResponseDTO loan : overdueLoans) {
                    log.warn("Overdue Loan - Number: {}, Customer: {}, Amount: â‚¹{}, Days Overdue: {}",
                            loan.getLoanNumber(),
                            loan.getCustomerName(),
                            loan.getOutstandingAmount(),
                            loan.getOverdueDays());
                }
            } else {
                log.info("No overdue loans found");
            }
        } catch (Exception e) {
            log.error("Error generating overdue loan report", e);
        }
    }
}
