package com.tss.banking.service;

/**
 * Service interface for notification management
 */
public interface NotificationService {
    
    /**
     * Send email notification
     * @param to Recipient email
     * @param subject Email subject
     * @param message Email message
     * @return true if sent successfully, false otherwise
     */
    boolean sendEmail(String to, String subject, String message);
    
    /**
     * Send SMS notification
     * @param phoneNumber Recipient phone number
     * @param message SMS message
     * @return true if sent successfully, false otherwise
     */
    boolean sendSMS(String phoneNumber, String message);
    
    /**
     * Send transaction notification
     * @param customerId Customer ID
     * @param transactionType Type of transaction
     * @param amount Transaction amount
     * @param accountNumber Account number
     */
    void sendTransactionNotification(Long customerId, String transactionType, String amount, String accountNumber);
    
    /**
     * Send account creation notification
     * @param customerId Customer ID
     * @param accountNumber New account number
     * @param accountType Account type
     */
    void sendAccountCreationNotification(Long customerId, String accountNumber, String accountType);
    
    /**
     * Send password change notification
     * @param customerId Customer ID
     */
    void sendPasswordChangeNotification(Long customerId);
    
    /**
     * Send login notification
     * @param customerId Customer ID
     * @param ipAddress Login IP address
     * @param deviceInfo Device information
     */
    void sendLoginNotification(Long customerId, String ipAddress, String deviceInfo);
    
    /**
     * Send account status change notification
     * @param customerId Customer ID
     * @param newStatus New account status
     */
    void sendAccountStatusChangeNotification(Long customerId, String newStatus);
    
    /**
     * Send low balance alert
     * @param customerId Customer ID
     * @param accountNumber Account number
     * @param currentBalance Current balance
     * @param threshold Threshold amount
     */
    void sendLowBalanceAlert(Long customerId, String accountNumber, String currentBalance, String threshold);
    
    /**
     * Send daily transaction limit alert
     * @param customerId Customer ID
     * @param accountNumber Account number
     * @param currentUsage Current usage amount
     * @param limit Daily limit
     */
    void sendDailyLimitAlert(Long customerId, String accountNumber, String currentUsage, String limit);
    
    /**
     * Send welcome email to new customer
     * @param customerId Customer ID
     */
    void sendWelcomeEmail(Long customerId);
    
    /**
     * Send account closure notification
     * @param customerId Customer ID
     * @param accountNumber Closed account number
     */
    void sendAccountClosureNotification(Long customerId, String accountNumber);
    
    /**
     * Send monthly statement notification
     * @param customerId Customer ID
     * @param statementPeriod Statement period
     */
    void sendMonthlyStatementNotification(Long customerId, String statementPeriod);
    
    /**
     * Send security alert
     * @param customerId Customer ID
     * @param alertType Type of security alert
     * @param alertMessage Alert message
     */
    void sendSecurityAlert(Long customerId, String alertType, String alertMessage);
    
    /**
     * Send bulk notification to multiple customers
     * @param customerIds List of customer IDs
     * @param subject Notification subject
     * @param message Notification message
     * @param notificationType Type of notification (EMAIL/SMS/BOTH)
     */
    void sendBulkNotification(java.util.List<Long> customerIds, String subject, String message, String notificationType);
    
    /**
     * Schedule notification for later delivery
     * @param customerId Customer ID
     * @param subject Notification subject
     * @param message Notification message
     * @param scheduledTime Scheduled delivery time
     * @param notificationType Type of notification
     */
    void scheduleNotification(Long customerId, String subject, String message, 
                            java.time.LocalDateTime scheduledTime, String notificationType);
    
    /**
     * Get notification preferences for customer
     * @param customerId Customer ID
     * @return Notification preferences
     */
    NotificationPreferences getNotificationPreferences(Long customerId);
    
    /**
     * Update notification preferences for customer
     * @param customerId Customer ID
     * @param preferences New preferences
     */
    void updateNotificationPreferences(Long customerId, NotificationPreferences preferences);
    
    /**
     * Check if notification type is enabled for customer
     * @param customerId Customer ID
     * @param notificationType Type of notification
     * @return true if enabled, false otherwise
     */
    boolean isNotificationEnabled(Long customerId, String notificationType);
    
    /**
     * Inner class for notification preferences
     */
    class NotificationPreferences {
        private boolean emailEnabled;
        private boolean smsEnabled;
        private boolean transactionAlerts;
        private boolean securityAlerts;
        private boolean marketingEmails;
        private boolean monthlyStatements;
        private boolean lowBalanceAlerts;
        private boolean loginAlerts;
        
        // Constructors
        public NotificationPreferences() {
            // Default preferences
            this.emailEnabled = true;
            this.smsEnabled = false;
            this.transactionAlerts = true;
            this.securityAlerts = true;
            this.marketingEmails = false;
            this.monthlyStatements = true;
            this.lowBalanceAlerts = true;
            this.loginAlerts = false;
        }
        
        // Getters and setters
        public boolean isEmailEnabled() { return emailEnabled; }
        public void setEmailEnabled(boolean emailEnabled) { this.emailEnabled = emailEnabled; }
        
        public boolean isSmsEnabled() { return smsEnabled; }
        public void setSmsEnabled(boolean smsEnabled) { this.smsEnabled = smsEnabled; }
        
        public boolean isTransactionAlerts() { return transactionAlerts; }
        public void setTransactionAlerts(boolean transactionAlerts) { this.transactionAlerts = transactionAlerts; }
        
        public boolean isSecurityAlerts() { return securityAlerts; }
        public void setSecurityAlerts(boolean securityAlerts) { this.securityAlerts = securityAlerts; }
        
        public boolean isMarketingEmails() { return marketingEmails; }
        public void setMarketingEmails(boolean marketingEmails) { this.marketingEmails = marketingEmails; }
        
        public boolean isMonthlyStatements() { return monthlyStatements; }
        public void setMonthlyStatements(boolean monthlyStatements) { this.monthlyStatements = monthlyStatements; }
        
        public boolean isLowBalanceAlerts() { return lowBalanceAlerts; }
        public void setLowBalanceAlerts(boolean lowBalanceAlerts) { this.lowBalanceAlerts = lowBalanceAlerts; }
        
        public boolean isLoginAlerts() { return loginAlerts; }
        public void setLoginAlerts(boolean loginAlerts) { this.loginAlerts = loginAlerts; }
    }
}
