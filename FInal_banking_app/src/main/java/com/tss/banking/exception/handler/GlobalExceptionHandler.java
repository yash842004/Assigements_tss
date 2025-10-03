package com.tss.banking.exception.handler;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import com.tss.banking.dto.response.ErrorResponse;
import com.tss.banking.dto.response.ErrorResponse.ValidationError;
import com.tss.banking.exception.AccountInactiveException;
import com.tss.banking.exception.AccountNotFoundException;
import com.tss.banking.exception.AuthenticationException;
import com.tss.banking.exception.AuthorizationException;
import com.tss.banking.exception.BankingException;
import com.tss.banking.exception.BusinessRuleViolationException;
import com.tss.banking.exception.CustomerNotFoundException;
import com.tss.banking.exception.DatabaseOperationException;
import com.tss.banking.exception.DuplicateResourceException;
import com.tss.banking.exception.ExternalServiceException;
import com.tss.banking.exception.InsufficientFundsException;
import com.tss.banking.exception.InvalidTransactionAmountException;
import com.tss.banking.exception.TransactionNotFoundException;
import com.tss.banking.exception.ValidationException;
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFoundException(
            CustomerNotFoundException ex, WebRequest request) {
        logger.warn("Customer not found: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFoundException(
            AccountNotFoundException ex, WebRequest request) {
        logger.warn("Account not found: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTransactionNotFoundException(
            TransactionNotFoundException ex, WebRequest request) {
        logger.warn("Transaction not found: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientFundsException(
            InsufficientFundsException ex, WebRequest request) {
        logger.warn("Insufficient funds: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidTransactionAmountException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTransactionAmountException(
            InvalidTransactionAmountException ex, WebRequest request) {
        logger.warn("Invalid transaction amount: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AccountInactiveException.class)
    public ResponseEntity<ErrorResponse> handleAccountInactiveException(
            AccountInactiveException ex, WebRequest request) {
        logger.warn("Account inactive: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
            DuplicateResourceException ex, WebRequest request) {
        logger.warn("Duplicate resource: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRuleViolationException(
            BusinessRuleViolationException ex, WebRequest request) {
        logger.warn("Business rule violation: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException ex, WebRequest request) {
        logger.warn("Authentication failed: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler({AuthorizationException.class, AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleAuthorizationException(
            Exception ex, WebRequest request) {
        logger.warn("Access denied: {}", ex.getMessage());
        String errorCode = ex instanceof AuthorizationException ?
            ((AuthorizationException) ex).getErrorCode() : "ACCESS_DENIED";
        ErrorResponse errorResponse = new ErrorResponse(
            errorCode,
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException ex, WebRequest request) {
        logger.warn("Validation error: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseOperationException(
            DatabaseOperationException ex, WebRequest request) {
        logger.error("Database operation failed: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getErrorCode(),
            "Database operation failed. Please try again later.",
            request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponse> handleExternalServiceException(
            ExternalServiceException ex, WebRequest request) {
        logger.error("External service error: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getErrorCode(),
            "External service temporarily unavailable. Please try again later.",
            request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }
    @ExceptionHandler(BankingException.class)
    public ResponseEntity<ErrorResponse> handleBankingException(
            BankingException ex, WebRequest request) {
        logger.warn("Banking exception: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {
        logger.warn("Validation failed: {}", ex.getMessage());
        List<ValidationError> validationErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(this::mapFieldError)
            .collect(Collectors.toList());
        ErrorResponse errorResponse = new ErrorResponse(
            "VALIDATION_ERROR",
            "Validation failed for one or more fields",
            request.getDescription(false).replace("uri=", ""),
            validationErrors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(
            BindException ex, WebRequest request) {
        logger.warn("Bind exception: {}", ex.getMessage());
        List<ValidationError> validationErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(this::mapFieldError)
            .collect(Collectors.toList());
        ErrorResponse errorResponse = new ErrorResponse(
            "VALIDATION_ERROR",
            "Validation failed for one or more fields",
            request.getDescription(false).replace("uri=", ""),
            validationErrors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, WebRequest request) {
        logger.error("Data integrity violation: {}", ex.getMessage());
        String message = "Data integrity constraint violated";
        String errorCode = "DATA_INTEGRITY_VIOLATION";
        String rootCause = "";
        Throwable rootCauseException = ex.getRootCause();
        if (rootCauseException != null && rootCauseException.getMessage() != null) {
            rootCause = rootCauseException.getMessage();
        }
        if (rootCause.contains("Duplicate entry")) {
            message = "Duplicate entry found. Resource already exists.";
            errorCode = "DUPLICATE_RESOURCE";
        } else if (rootCause.contains("foreign key constraint")) {
            message = "Cannot perform operation. Referenced data exists.";
            errorCode = "FOREIGN_KEY_VIOLATION";
        }
        ErrorResponse errorResponse = new ErrorResponse(
            errorCode,
            message,
            request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, WebRequest request) {
        logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
            "INTERNAL_SERVER_ERROR",
            "An unexpected error occurred. Please try again later.",
            request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private ValidationError mapFieldError(FieldError fieldError) {
        return new ValidationError(
            fieldError.getField(),
            fieldError.getDefaultMessage(),
            fieldError.getRejectedValue()
        );
    }
}
