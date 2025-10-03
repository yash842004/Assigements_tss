package com.tss.banking.service.impl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tss.banking.service.FixedDepositService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@RequiredArgsConstructor
@Slf4j
public class FDScheduledTaskService {
    private final FixedDepositService fdService;
    @Scheduled(cron = "0 0 6 * * ?")
    @Transactional
    public void processFDMaturityAndRenewal() {
        log.info("Starting scheduled FD maturity and auto-renewal processing");
        try {
            fdService.markMaturedFDs();
            fdService.processAutoRenewals();
            log.info("Completed scheduled FD maturity and auto-renewal processing");
        } catch (Exception e) {
            log.error("Error during scheduled FD processing", e);
        }
    }
    @Scheduled(cron = "0 0 9-18 * * MON-FRI")
    public void logFDStatistics() {
        try {
            Long activeFDCount = fdService.getActiveFDCount();
            var totalAmount = fdService.getTotalActiveFDAmount();
            log.info("FD Statistics - Active FDs: {}, Total Amount: â‚¹{}",
                    activeFDCount, totalAmount);
        } catch (Exception e) {
            log.error("Error logging FD statistics", e);
        }
    }
}
