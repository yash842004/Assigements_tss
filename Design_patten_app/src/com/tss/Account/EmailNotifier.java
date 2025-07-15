package com.tss.Account;

public class EmailNotifier implements INotifier {

	@Override
	public void sendNotification(Account account) {
		System.out.println("Email Notification: Account " + account.getAccountNumber() + 
						" | Balance: " + account.getBalance());

	}

}
