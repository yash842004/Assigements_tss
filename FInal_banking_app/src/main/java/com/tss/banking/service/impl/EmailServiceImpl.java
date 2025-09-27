package com.tss.banking.service.impl;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.tss.banking.entity.Account;
import com.tss.banking.entity.Transaction;
import com.tss.banking.service.EmailService;


@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @org.springframework.beans.factory.annotation.Value("${banking.email.notifications.enabled:true}")
    private boolean emailNotificationsEnabled;

    private static final String FROM_EMAIL = "yashbhimani842004@gmail.com";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    public void sendTransactionNotification(Transaction transaction, Account account) {
        try {
            String subject = "Transaction Alert - " + transaction.getTransactionType();
            String body = buildTransactionEmailBody(transaction, account);
            
            sendEmail(account.getCustomer().getEmail(), subject, body);
            logger.info("Transaction notification sent for transaction ID: {}", transaction.getId());
        } catch (Exception e) {
            logger.error("Failed to send transaction notification for transaction ID: {}", 
                        transaction.getId(), e);
        }
    }

    @Override
    public void sendBalanceUpdateNotification(String email, String accountNumber, 
                                            BigDecimal previousBalance, BigDecimal currentBalance, 
                                            BigDecimal transactionAmount, String transactionType) {
        try {
            String subject = "Account Balance Update - " + transactionType;
            String body = buildBalanceUpdateEmailBody(accountNumber, previousBalance, 
                                                    currentBalance, transactionAmount, transactionType);
            
            sendEmail(email, subject, body);
            logger.info("Balance update notification sent to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send balance update notification to: {}", email, e);
        }
    }

    @Override
    public void sendLoanDisbursementNotification(String email, String customerName, 
                                               BigDecimal loanAmount, String accountNumber) {
        try {
            String subject = "Loan Disbursement Successful";
            String body = String.format(
                "Dear %s,\n\n" +
                "Your loan has been approved and disbursed successfully!\n\n" +
                "Loan Amount: ₹%.2f\n" +
                "Credited to Account: %s\n" +
                "Transaction Date: %s\n\n" +
                "The loan amount has been credited to your account. Please check your account balance.\n\n" +
                "Thank you for banking with TSS Bank.\n\n" +
                "Best regards,\n" +
                "TSS Bank Team",
                customerName, loanAmount, accountNumber, 
                java.time.LocalDateTime.now().format(DATE_FORMATTER)
            );
            
            sendEmail(email, subject, body);
            logger.info("Loan disbursement notification sent to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send loan disbursement notification to: {}", email, e);
        }
    }

    @Override
    public void sendEMIDeductionNotification(String email, String customerName, 
                                           BigDecimal emiAmount, BigDecimal remainingBalance) {
        try {
            String subject = "EMI Payment Processed";
            String body = String.format(
                "Dear %s,\n\n" +
                "Your EMI payment has been processed successfully.\n\n" +
                "EMI Amount Debited: ₹%.2f\n" +
                "Remaining Loan Balance: ₹%.2f\n" +
                "Payment Date: %s\n\n" +
                "Thank you for your timely payment.\n\n" +
                "Best regards,\n" +
                "TSS Bank Team",
                customerName, emiAmount, remainingBalance,
                java.time.LocalDateTime.now().format(DATE_FORMATTER)
            );
            
            sendEmail(email, subject, body);
            logger.info("EMI deduction notification sent to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send EMI deduction notification to: {}", email, e);
        }
    }

    @Override
    public void sendFDCreationNotification(String email, String customerName, 
                                         BigDecimal fdAmount, String accountNumber, 
                                         BigDecimal accountBalance) {
        try {
            String subject = "Fixed Deposit Created Successfully";
            String body = String.format(
                "Dear %s,\n\n" +
                "Your Fixed Deposit has been created successfully!\n\n" +
                "FD Amount: ₹%.2f\n" +
                "Debited from Account: %s\n" +
                "Current Account Balance: ₹%.2f\n" +
                "Transaction Date: %s\n\n" +
                "Your Fixed Deposit is now active and earning interest.\n\n" +
                "Thank you for banking with TSS Bank.\n\n" +
                "Best regards,\n" +
                "TSS Bank Team",
                customerName, fdAmount, accountNumber, accountBalance,
                java.time.LocalDateTime.now().format(DATE_FORMATTER)
            );
            
            sendEmail(email, subject, body);
            logger.info("FD creation notification sent to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send FD creation notification to: {}", email, e);
        }
    }

    @Override
    public void sendFDMaturityNotification(String email, String customerName, 
                                         BigDecimal maturityAmount, String accountNumber) {
        try {
            String subject = "Fixed Deposit Matured";
            String body = String.format(
                "Dear %s,\n\n" +
                "Your Fixed Deposit has matured and the amount has been credited to your account.\n\n" +
                "Maturity Amount Credited: ₹%.2f\n" +
                "Credited to Account: %s\n" +
                "Transaction Date: %s\n\n" +
                "Thank you for banking with TSS Bank.\n\n" +
                "Best regards,\n" +
                "TSS Bank Team",
                customerName, maturityAmount, accountNumber,
                java.time.LocalDateTime.now().format(DATE_FORMATTER)
            );
            
            sendEmail(email, subject, body);
            logger.info("FD maturity notification sent to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send FD maturity notification to: {}", email, e);
        }
    }

    private String buildTransactionEmailBody(Transaction transaction, Account account) {
        return String.format(
            "Dear %s,\n\n" +
            "A transaction has been processed on your account.\n\n" +
            "Transaction Details:\n" +
            "- Transaction Type: %s\n" +
            "- Amount: ₹%.2f\n" +
            "- Account Number: %s\n" +
            "- Previous Balance: ₹%.2f\n" +
            "- Current Balance: ₹%.2f\n" +
            "- Transaction Date: %s\n" +
            "- Reference ID: %s\n" +
            "- Description: %s\n\n" +
            "If you did not authorize this transaction, please contact us immediately.\n\n" +
            "Thank you for banking with TSS Bank.\n\n" +
            "Best regards,\n" +
            "TSS Bank Team",
            account.getCustomer().getFirstName(),
            transaction.getTransactionType(),
            transaction.getAmount(),
            account.getAccountNumber(),
            transaction.getBalanceAfter().subtract(getTransactionImpact(transaction)),
            transaction.getBalanceAfter(),
            transaction.getTransactionDate().format(DATE_FORMATTER),
            transaction.getReferenceId(),
            transaction.getDescription() != null ? transaction.getDescription() : "N/A"
        );
    }

    private String buildBalanceUpdateEmailBody(String accountNumber, BigDecimal previousBalance, 
                                             BigDecimal currentBalance, BigDecimal transactionAmount, 
                                             String transactionType) {
        return String.format(
            "Dear Customer,\n\n" +
            "Your account balance has been updated.\n\n" +
            "Transaction Details:\n" +
            "- Transaction Type: %s\n" +
            "- Amount: ₹%.2f\n" +
            "- Account Number: %s\n" +
            "- Previous Balance: ₹%.2f\n" +
            "- Current Balance: ₹%.2f\n" +
            "- Transaction Date: %s\n\n" +
            "Thank you for banking with TSS Bank.\n\n" +
            "Best regards,\n" +
            "TSS Bank Team",
            transactionType, transactionAmount, accountNumber, 
            previousBalance, currentBalance,
            java.time.LocalDateTime.now().format(DATE_FORMATTER)
        );
    }

    private BigDecimal getTransactionImpact(Transaction transaction) {
        return switch (transaction.getTransactionType()) {
            case DEPOSIT, LOAN_DISBURSEMENT, TRANSFER_IN -> transaction.getAmount();
            case WITHDRAWAL, TRANSFER_OUT, LOAN_PAYMENT -> transaction.getAmount().negate();
            default -> BigDecimal.ZERO;
        };
    }

    private void sendEmail(String to, String subject, String body) {
        if (!emailNotificationsEnabled) {
            logger.info("Email notifications are disabled. Skipping email to: {}", to);
            return;
        }
        
        try {
            logger.info("Attempting to send email to: {} with subject: {}", to, subject);
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(FROM_EMAIL);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            
            mailSender.send(message);
            logger.info("Email sent successfully to: {}", to);
            
        } catch (Exception e) {
            logger.error("Failed to send email to: {} - Error: {}", to, e.getMessage(), e);
            // Don't throw exception to avoid breaking the transaction
        }
    }
}