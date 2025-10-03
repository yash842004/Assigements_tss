package com.tss.banking.service;
public interface NotificationService {
    boolean sendEmail(String to, String subject, String message);
    boolean sendSMS(String phoneNumber, String message);
    void sendTransactionNotification(Long customerId, String transactionType, String amount, String accountNumber);
    void sendAccountCreationNotification(Long customerId, String accountNumber, String accountType);
    void sendPasswordChangeNotification(Long customerId);
    void sendLoginNotification(Long customerId, String ipAddress, String deviceInfo);
    void sendAccountStatusChangeNotification(Long customerId, String newStatus);
    void sendLowBalanceAlert(Long customerId, String accountNumber, String currentBalance, String threshold);
    void sendDailyLimitAlert(Long customerId, String accountNumber, String currentUsage, String limit);
    void sendWelcomeEmail(Long customerId);
    void sendAccountClosureNotification(Long customerId, String accountNumber);
    void sendMonthlyStatementNotification(Long customerId, String statementPeriod);
    void sendSecurityAlert(Long customerId, String alertType, String alertMessage);
    void sendBulkNotification(java.util.List<Long> customerIds, String subject, String message, String notificationType);
    void scheduleNotification(Long customerId, String subject, String message,
                            java.time.LocalDateTime scheduledTime, String notificationType);
    NotificationPreferences getNotificationPreferences(Long customerId);
    void updateNotificationPreferences(Long customerId, NotificationPreferences preferences);
    boolean isNotificationEnabled(Long customerId, String notificationType);
    class NotificationPreferences {
        private boolean emailEnabled;
        private boolean smsEnabled;
        private boolean transactionAlerts;
        private boolean securityAlerts;
        private boolean marketingEmails;
        private boolean monthlyStatements;
        private boolean lowBalanceAlerts;
        private boolean loginAlerts;
        public NotificationPreferences() {
            this.emailEnabled = true;
            this.smsEnabled = false;
            this.transactionAlerts = true;
            this.securityAlerts = true;
            this.marketingEmails = false;
            this.monthlyStatements = true;
            this.lowBalanceAlerts = true;
            this.loginAlerts = false;
        }
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
