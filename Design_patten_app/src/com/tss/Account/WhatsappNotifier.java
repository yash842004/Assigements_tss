package com.tss.Account;

public class WhatsappNotifier implements INotifier {

	@Override
	public void sendNotification(Account account) {
		System.out.println("WhatsApp Notification: Account " + account.getAccountNumber() +
							" | Balance: " + account.getBalance());

	}

}
