package com.tss.Model;

import java.util.Scanner;

public class MainForBooks {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Book book1 = new Book();
		System.out.println("Enter the Book Id: ");
		book1.setBookId(scanner.nextInt());
		scanner.nextLine(); 

		System.out.println("Enter the Name of the book: ");
		book1.setName(scanner.nextLine());

		System.out.println("Enter the Author of the book: ");
		book1.setAuthor(scanner.nextLine());

		System.out.println("Enter the Publication of the book: ");
		book1.setPublication(scanner.nextLine());

		System.out.println("Enter the Price of the book: ");
		book1.setPrice(scanner.nextInt());

		book1.display();

	}

}
