package com.tss.jpa.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Data
@RequiredArgsConstructor
public class ResponseError {
	
	private int status;
	private long timestamp;
	private String message;

}
