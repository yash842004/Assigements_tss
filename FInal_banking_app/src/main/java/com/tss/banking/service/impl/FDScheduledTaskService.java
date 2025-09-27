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

    /**
     * Process FD maturities and auto-renewals daily at 6 AM
     */
    @Scheduled(cron = "0 0 6 * * ?")
    @Transactional
    public void processFDMaturityAndRenewal() {
        log.info("Starting scheduled FD maturity and auto-renewal processing");
        
        try {
            // Mark FDs as matured
            fdService.markMaturedFDs();
            
            // Process auto-renewals
            fdService.processAutoRenewals();
            
            log.info("Completed scheduled FD maturity and auto-renewal processing");
        } catch (Exception e) {
            log.error("Error during scheduled FD processing", e);
        }
    }
    
    /**
     * Log FD statistics hourly during business hours (9 AM to 6 PM)
     */
    @Scheduled(cron = "0 0 9-18 * * MON-FRI")
    public void logFDStatistics() {
        try {
            Long activeFDCount = fdService.getActiveFDCount();
            var totalAmount = fdService.getTotalActiveFDAmount();
            
            log.info("FD Statistics - Active FDs: {}, Total Amount: ₹{}", 
                    activeFDCount, totalAmount);
        } catch (Exception e) {
            log.error("Error logging FD statistics", e);
        }
    }
}
