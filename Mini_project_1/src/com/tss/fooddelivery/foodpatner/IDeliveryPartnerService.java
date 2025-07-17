package com.tss.fooddelivery.foodpatner;

import java.util.Scanner;

import com.tss.fooddelivery.customer.Address;

public interface IDeliveryPartnerService {
	void addDeliveryPartner(Scanner scanner);
    void viewDeliveryPartners();
    void deliverOrder(Address address);
}
