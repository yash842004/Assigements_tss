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
import com.tss.banking.entity.Customer;
import com.tss.banking.entity.Transaction;
import com.tss.banking.service.EmailService;
@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    @Autowired(required = false)
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
                "Loan Amount: â‚¹%.2f\n" +
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
                "EMI Amount Debited: â‚¹%.2f\n" +
                "Remaining Loan Balance: â‚¹%.2f\n" +
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
                "FD Amount: â‚¹%.2f\n" +
                "Debited from Account: %s\n" +
                "Current Account Balance: â‚¹%.2f\n" +
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
                "Maturity Amount Credited: â‚¹%.2f\n" +
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
            "- Amount: â‚¹%.2f\n" +
            "- Account Number: %s\n" +
            "- Previous Balance: â‚¹%.2f\n" +
            "- Current Balance: â‚¹%.2f\n" +
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
            "- Amount: â‚¹%.2f\n" +
            "- Account Number: %s\n" +
            "- Previous Balance: â‚¹%.2f\n" +
            "- Current Balance: â‚¹%.2f\n" +
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
        
        if (mailSender == null) {
            logger.warn("JavaMailSender is not configured. Email notification would be sent to: {} with subject: {}", to, subject);
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
        }
    }
    @Override
    public void sendJointAccountCreationNotification(Customer customer, Account account, boolean isPrimary) {
        logger.info("Sending joint account creation notification to customer: {} for account: {}",
                    customer.getId(), account.getAccountNumber());
        String subject = "Joint Account Successfully Created - " + account.getAccountNumber();
        String role = isPrimary ? "Primary Account Holder" : "Joint Account Holder";
        String body = String.format("""
            Dear %s,
            We are pleased to inform you that a joint account has been successfully created.
            Account Details:
            - Account Number: %s
            - Account Type: %s
            - Your Role: %s
            - Current Balance: â‚¹%.2f
            - Creation Date: %s
            %s
            Important Information:
            - All account holders can perform transactions on this account
            - You will receive notifications for all transactions
            - Please keep your account details secure
            If you have any questions or concerns, please contact our customer support.
            Thank you for banking with us.
            Best regards,
            Banking Team
            """,
            customer.getFirstName() + " " + customer.getLastName(),
            account.getAccountNumber(),
            account.getAccountType(),
            role,
            account.getBalance(),
            account.getCreatedDate().format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")),
            isPrimary ? "As the primary account holder, you have full administrative privileges for this account."
                      : "You have been added as a joint holder to this account."
        );
        sendEmail(customer.getEmail(), subject, body);
    }
    @Override
    public void sendJointAccountHolderAddedNotification(Customer customer, Account account) {
        logger.info("Sending joint account holder added notification to customer: {} for account: {}",
                    customer.getId(), account.getAccountNumber());
        String subject = "Added as Joint Account Holder - " + account.getAccountNumber();
        String body = String.format("""
            Dear %s,
            You have been successfully added as a joint account holder.
            Account Details:
            - Account Number: %s
            - Account Type: %s
            - Your Role: Joint Account Holder
            - Current Balance: â‚¹%.2f
            - Date Added: %s
            Account Privileges:
            - Make deposits and withdrawals
            - View account balance and transaction history
            - Receive transaction notifications
            - Access online banking services
            Important Notes:
            - You now have full transaction privileges on this account
            - All transactions will be notified to all account holders
            - Please keep your account credentials secure
            If you did not request to be added to this account or have any concerns,
            please contact our customer support immediately.
            Thank you for banking with us.
            Best regards,
            Banking Team
            """,
            customer.getFirstName() + " " + customer.getLastName(),
            account.getAccountNumber(),
            account.getAccountType(),
            account.getBalance(),
            java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
        );
        sendEmail(customer.getEmail(), subject, body);
    }
    @Override
    public void sendJointAccountHolderRemovedNotification(Customer customer, Account account) {
        logger.info("Sending joint account holder removed notification to customer: {} for account: {}",
                    customer.getId(), account.getAccountNumber());
        String subject = "Removed from Joint Account - " + account.getAccountNumber();
        String body = String.format("""
            Dear %s,
            This is to inform you that you have been removed as a joint account holder.
            Account Details:
            - Account Number: %s
            - Account Type: %s
            - Removal Date: %s
            Important Information:
            - You no longer have access to this account
            - Your transaction privileges have been revoked
            - You will not receive further notifications about this account
            - Any linked services (cards, online banking) for this account have been deactivated
            Account Access:
            - You can no longer make transactions on this account
            - Online banking access to this account has been removed
            - Please return any ATM/debit cards associated with this account
            If you believe this removal was made in error or have any questions,
            please contact our customer support team immediately.
            Thank you for banking with us.
            Best regards,
            Banking Team
            """,
            customer.getFirstName() + " " + customer.getLastName(),
            account.getAccountNumber(),
            account.getAccountType(),
            java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
        );
        sendEmail(customer.getEmail(), subject, body);
    }
    @Override
    public void sendCustomerApprovalNotification(Customer customer) {
        if (!emailNotificationsEnabled) {
            logger.info("Email notifications are disabled. Skipping customer approval notification for: {}", customer.getEmail());
            return;
        }
        logger.info("Sending customer approval notification to: {}", customer.getEmail());
        String subject = "Welcome to Our Banking System - Account Approved!";
        String body = String.format("""
            Dear %s,
            Congratulations! We are delighted to inform you that your account registration has been approved by our admin team.
            Account Details:
            - Customer ID: %d
            - Email: %s
            - Phone: %s
            - Registration Date: %s
            - Approval Date: %s
            What's Next:
            âœ“ Your account is now active and ready to use
            âœ“ You can now create bank accounts (Savings, Current, Joint Accounts)
            âœ“ Apply for loans and fixed deposits
            âœ“ Perform all banking transactions
            âœ“ Access all our banking services
            Getting Started:
            1. Login to your account using your registered email and password
            2. Create your first bank account
            3. Start using our comprehensive banking services
            Security Tips:
            - Keep your login credentials secure
            - Never share your password with anyone
            - Contact us immediately if you notice any suspicious activity
            If you have any questions or need assistance, our customer support team is here to help.
            Welcome aboard and thank you for choosing our banking services!
            Best regards,
            Banking System Team
            """,
            customer.getFirstName() + " " + customer.getLastName(),
            customer.getId(),
            customer.getEmail(),
            customer.getPhoneNumber(),
            customer.getRegistrationDate().format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")),
            java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
        );
        sendEmail(customer.getEmail(), subject, body);
    }
    @Override
    public void sendCustomerRejectionNotification(Customer customer, String rejectionReason) {
        if (!emailNotificationsEnabled) {
            logger.info("Email notifications are disabled. Skipping customer rejection notification for: {}", customer.getEmail());
            return;
        }
        logger.info("Sending customer rejection notification to: {}", customer.getEmail());
        String subject = "Account Registration Update - Further Review Required";
        String body = String.format("""
            Dear %s,
            Thank you for your interest in our banking services. We have reviewed your account registration application.
            Unfortunately, we are unable to approve your account at this time due to the following reason:
            Reason: %s
            Account Details:
            - Customer ID: %d
            - Email: %s
            - Phone: %s
            - Registration Date: %s
            - Review Date: %s
            What You Can Do:
            - Please review the reason mentioned above
            - If you believe this is an error, please contact our customer support
            - You may reapply after addressing the mentioned concerns
            Contact Information:
            - Email: support@banking.com
            - Phone: 1-800-BANKING
            - Visit our nearest branch for assistance
            We appreciate your understanding and look forward to serving you in the future.
            Best regards,
            Banking System Team
            """,
            customer.getFirstName() + " " + customer.getLastName(),
            rejectionReason != null ? rejectionReason : "Administrative review required",
            customer.getId(),
            customer.getEmail(),
            customer.getPhoneNumber(),
            customer.getRegistrationDate().format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")),
            java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
        );
        sendEmail(customer.getEmail(), subject, body);
    }
}
