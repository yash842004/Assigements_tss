package com.tss.fooddelivery.foodpatner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.tss.fooddelivery.customer.Address;

public class DeliveryPartnerService implements IDeliveryPartnerService {

	private static List<DeliveryPartner> deliveryPartners = new ArrayList<>();
	private static int nextPartnerId = 3;

	static {
		deliveryPartners.add(new DeliveryPartner(1, "Zomato"));
		deliveryPartners.add(new DeliveryPartner(2, "Swiggy"));
	}

	@Override
	public void addDeliveryPartner(Scanner scanner) {
		System.out.print("Enter Delivery Partner Name to add: ");
		String name = scanner.nextLine().trim();

		boolean exists = deliveryPartners.stream().anyMatch(partner -> partner.getName().equalsIgnoreCase(name));

		if (exists) {
			System.out.println("Delivery partner '" + name + "' already exists. Cannot add duplicate.");
			return;
		}

		deliveryPartners.add(new DeliveryPartner(nextPartnerId, name));
		System.out.println("Delivery partner " + name + " added successfully with ID: " + nextPartnerId);
		nextPartnerId++;

	}

	@Override
	public void viewDeliveryPartners() {
		System.out.println("\n--- Available Delivery Partners ---");

		if (deliveryPartners.isEmpty()) {
			System.out.println("No delivery partners available.");
			return;
		}

		deliveryPartners.stream().sorted((p1, p2) -> Integer.compare(p1.getId(), p2.getId()))
				.forEach(partner -> System.out.println(partner.getId() + ". " + partner.getName()));
	}

	@Override
	public void deliverOrder(Address address) {
		if (deliveryPartners.isEmpty()) {
			System.out.println("No delivery partners available to deliver your order.");
			return;
		}

		Random random = new Random();
		DeliveryPartner selectedPartner = deliveryPartners.get(random.nextInt(deliveryPartners.size()));

		System.out.println("\n--- Delivery Details ---");
		System.out.println("Your order will be delivered by: " + selectedPartner.getName());
		System.out.println("Delivery Address: " + address.toString());
		System.out.println("Our delivery partner is on the way. Thank you for ordering!");
	}

}
