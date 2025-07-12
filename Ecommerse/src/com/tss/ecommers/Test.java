package com.tss.ecommers;

import java.util.Date;

public class Test {

	public static void main(String[] args) {

		System.out.println("--- Creating Products ---");
		Product p1 = new Product(101, "Laptop", 1200.00, 10.0);
		Product p2 = new Product(102, "Mouse", 25.00, 0.0);
		Product p3 = new Product(103, "Keyboard", 75.00, 5.0);
		Product p4 = new Product(104, "Monitor", 300.00, 15.0);

		System.out.println("Product 1: " + p1.getName() + ", Original Price: " + p1.getPrice() + ", Discounted Price: "
				+ String.format("%.2f", p1.calculateDiscountedPrice()));
		System.out.println("Product 2: " + p2.getName() + ", Original Price: " + p2.getPrice() + ", Discounted Price: "
				+ String.format("%.2f", p2.calculateDiscountedPrice()));
		System.out.println("Product 3: " + p3.getName() + ", Original Price: " + p3.getPrice() + ", Discounted Price: "
				+ String.format("%.2f", p3.calculateDiscountedPrice()));
		System.out.println("Product 4: " + p4.getName() + ", Original Price: " + p4.getPrice() + ", Discounted Price: "
				+ String.format("%.2f", p4.calculateDiscountedPrice()));

		System.out.println("\n--- Creating LineItems ---");
		LineItem li1 = new LineItem(1, 1, p1);
		LineItem li2 = new LineItem(2, 2, p2);
		LineItem li3 = new LineItem(3, 1, p3);
		LineItem li4 = new LineItem(4, 3, p4);

		System.out.println("LineItem 1: " + li1);
		System.out.println("LineItem 2: " + li2);
		System.out.println("LineItem 3: " + li3);
		System.out.println("LineItem 4: " + li4);

		System.out.println("\n--- Creating a Shopping Cart and adding LineItems ---");
		ShoppingCart myCart = new ShoppingCart();
		myCart.addLineItem(li1);
		myCart.addLineItem(li2);
		myCart.addLineItem(li3);
		myCart.addLineItem(li4);

		myCart.displayCartContents();

		System.out.println("\n--- Creating an Order from LineItems ---");
		Order customerOrder = new Order(201, new Date());
		customerOrder.addLineItem(li1);
		customerOrder.addLineItem(li2);

		customerOrder.addLineItem(li3);
		customerOrder.addLineItem(li4);

		System.out.println("Order created: " + customerOrder);
		System.out.println("Order Total Price: " + String.format("%.2f", customerOrder.calculateOrderPrice()));

		System.out.println("\n--- Creating a Customer and adding Orders ---");
		Customer customer = new Customer(301, "John Doe", "456 Oak Ave, Rajkot");
		customer.addOrder(customerOrder);

		Order anotherOrder = new Order(202, new Date());
		Product p5 = new Product(105, "Webcam", 50.00, 0.0);
		LineItem li5 = new LineItem(5, 1, p5);
		anotherOrder.addLineItem(li5);
		customer.addOrder(anotherOrder);

		System.out.println("Customer created: " + customer);
		System.out.println("Customer's First Order Details: " + customer.getOrders().get(0));
		System.out.println("Customer's Second Order Details: " + customer.getOrders().get(1));

	}

}
