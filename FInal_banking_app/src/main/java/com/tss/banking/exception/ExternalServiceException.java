package com.tss.banking.exception;

/**
 * Exception thrown when external service operations fail
 */
public class ExternalServiceException extends BankingException {
    
    public ExternalServiceException(String service, String message) {
        super(String.format("External service '%s' error: %s", service, message), "EXTERNAL_SERVICE_ERROR");
    }
    
    public ExternalServiceException(String service, String message, Throwable cause) {
        super(String.format("External service '%s' error: %s", service, message), "EXTERNAL_SERVICE_ERROR", cause);
    }
    
    public static ExternalServiceException paymentGateway(String message) {
        return new ExternalServiceException("Payment Gateway", message);
    }
    
    public static ExternalServiceException emailService(String message) {
        return new ExternalServiceException("Email Service", message);
    }
    
    public static ExternalServiceException smsService(String message) {
        return new ExternalServiceException("SMS Service", message);
    }
    
    public static ExternalServiceException auditService(String message) {
        return new ExternalServiceException("Audit Service", message);
    }
}
