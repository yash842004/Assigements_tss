package com.tss.model.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Problem_7 {

	public static void main(String[] args) {
		  List<String> emailAddresses = Arrays.asList(
		            "raj@example.com",
		            "rohan@gmail.com",
		            "raj@yahoo.com",
		            "yash@outlook.com",
		            "dev@example.com", 
		            "deep@gmail.com",     
		            "dipak@mymail.co.us",
		            "priya@internal.net"
		        );

		        System.out.println("Getting Unique Email Domains ---");
		        System.out.println("Input Email Addresses: " + emailAddresses);

		        Set<String> unique = emailAddresses.stream()
		         
		            .map(email -> email.substring(email.indexOf("@") + 1))
		     
		            .collect(Collectors.toSet());

		        System.out.println("Unique Email Domains: " + unique);
		        
		    
	}

}
