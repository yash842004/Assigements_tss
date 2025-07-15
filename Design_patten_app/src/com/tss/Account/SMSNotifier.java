package com.tss.Account;

public class SMSNotifier implements INotifier {

	@Override
	public void sendNotification(Account account) {
		System.out.println("SMS Notification: Account " + account.getAccountNumber()
							+ " | Balance: " + account.getBalance());		
	}

}
