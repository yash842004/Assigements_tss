package com.tss.fooddelivery.Database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.tss.fooddelivery.customer.Address;

public class CustomerData {
	private static final String FILE_NAME = "customerData.ser";

	public static void saveCustomer(Address address) {
		List<Address> customers = loadCustomers();
		customers.add(address);

		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
			out.writeObject(customers);
			System.out.println("Customer details saved successfully.");
		} catch (IOException exception) {
			System.out.println("Error saving customer details: " + exception.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public static List<Address> loadCustomers() {
		List<Address> customers = new ArrayList<>();

		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
			customers = (List<Address>) in.readObject();
		} catch (FileNotFoundException exception) {
		} catch (IOException | ClassNotFoundException exception) {
			System.out.println("Error loading customer details: " + exception.getMessage());
		}

		return customers;

	}
}
