package com.tss.jpa.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Data
@RequiredArgsConstructor
public class AgeException extends RuntimeException {
    private String message;

    @Override
    public String getMessage() {
        return message;
    }
}
