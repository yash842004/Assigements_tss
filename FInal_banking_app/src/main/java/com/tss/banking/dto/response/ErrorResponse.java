package com.tss.banking.dto.response;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
public class ErrorResponse {
    private String errorCode;
    private String message;
    private String path;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private List<ValidationError> validationErrors;
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }
    public ErrorResponse(String errorCode, String message, String path) {
        this();
        this.errorCode = errorCode;
        this.message = message;
        this.path = path;
    }
    public ErrorResponse(String errorCode, String message, String path, List<ValidationError> validationErrors) {
        this(errorCode, message, path);
        this.validationErrors = validationErrors;
    }
    public String getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }
    public void setValidationErrors(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }
    public static class ValidationError {
        private String field;
        private String message;
        private Object rejectedValue;
        public ValidationError() {}
        public ValidationError(String field, String message, Object rejectedValue) {
            this.field = field;
            this.message = message;
            this.rejectedValue = rejectedValue;
        }
        public String getField() {
            return field;
        }
        public void setField(String field) {
            this.field = field;
        }
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
        public Object getRejectedValue() {
            return rejectedValue;
        }
        public void setRejectedValue(Object rejectedValue) {
            this.rejectedValue = rejectedValue;
        }
    }
}
