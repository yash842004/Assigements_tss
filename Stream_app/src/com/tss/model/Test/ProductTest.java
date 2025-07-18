package com.tss.model.Test;

import java.util.Arrays;
import java.util.List;

import com.tss.model.Product;

public class ProductTest {

	public static void main(String args[]) {
		List<Product> products = Arrays.asList(new Product("Laptop", 50000, 2), new Product("Mouse", 500, 3),
				new Product("Keyboard", 1500, 1));

		double totalBill = products.stream().map(price -> price.getPrice() * price.getQuantity()).reduce(0.0,
							(sum, itemTotal) -> sum + itemTotal);

		System.out.println("Total Bill Amount: " + totalBill);
	}

}
