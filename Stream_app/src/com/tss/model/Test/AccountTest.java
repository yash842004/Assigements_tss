package com.tss.model.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.tss.model.Account;

public class AccountTest {

	public static void main(String[] args) {

		
		 List<Account> accounts = new ArrayList<>();
	        accounts.add(new Account(1, "Ravi", 15000));
	        accounts.add(new Account(2, "Akshay", 30000));
	        accounts.add(new Account(3, "Prathamesh", 5000));
	        accounts.add(new Account(4, "Rohan", 45000));
	        accounts.add(new Account(5, "Amit", 25000));

	        Account minAcc = accounts.stream()
	                                 .min(Comparator.comparingDouble(Account::getBalance))
	                                 .orElse(null);
	        System.out.println("Account with minimum balance:");
	        System.out.println(minAcc);

	        Account maxAcc = accounts.stream()
	                                 .max(Comparator.comparingDouble(Account::getBalance))
	                                 .orElse(null);
	        System.out.println("Account with maximum balance:");
	        System.out.println(maxAcc);

	        System.out.println("Names greater than 6 characters:");
	        accounts.stream()
	                .filter(name -> name.getName().length() > 6)
	                .forEach(name -> System.out.println(name.getName()));

	        double totalBalance = accounts.stream()
	                                      .mapToDouble(Account::getBalance)
	                                      .sum();
	        System.out.println("Total balance of all accounts: " + totalBalance);
	    
	}

}
