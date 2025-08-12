package com.tss.bank.Controller;

import java.util.Scanner;
import com.tss.bank.service.TransferService;

public class TransferController {

    private TransferService service = new TransferService();
    
    public void startTransfer() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Sender's ID: ");
        int fromId = scanner.nextInt();

        System.out.print("Enter Receiver's ID: ");
        int toId = scanner.nextInt();

        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();

        service.transferAmount(fromId, toId, amount);
    }

}
