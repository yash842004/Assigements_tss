package collection_app.com.tss.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class BookTest {

	private static ArrayList<Book> books = new ArrayList<>();
	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {

		int choice;
		do {
			displayMenu();
			System.out.print("Enter your choice: ");
			choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				addBook();
				break;
			case 2:
				issueBook();
				break;
			case 3:
				displayAvailableBooks();
				break;
			case 4:
				displayAllIssuedBooks();
				break;
			case 5:
				returnBook();
				break;
			case 6:
				sortBooksMenu();
				break;
			case 0:
				System.out.println("Exiting Book Application. Goodbye!");
				break;
			}
			System.out.println("\n-----------------------------------\n");
		} while (choice != 0);

	}

	private static void displayMenu() {
		System.out.println("Book Application Menu:");
		System.out.println("1. Add a new book");
		System.out.println("2. Issue a book by ID");
		System.out.println("3. Display all available books");
		System.out.println("4. Display all issued books");
		System.out.println("5. Return a book");
		System.out.println("6. Sort Books");
		System.out.println("0. Exit");
	}

	private static void addBook() {
		System.out.print("Enter Book ID: ");
		String id = scanner.nextLine();
		System.out.print("Enter Book Title: ");
		String title = scanner.nextLine();
		System.out.print("Enter Book Author: ");
		String author = scanner.nextLine();

		for (Book book : books) {
			if (book.getId().equals(id)) {
				System.out.println("Error: Book with ID '" + id + "' already exists.");
				return;
			}
		}

		Book newBook = new Book(id, title, author);
		books.add(newBook);
		System.out.println("Book added successfully: " + newBook.getTitle());
	}

	private static void issueBook() {
		System.out.print("Enter the ID of the book to issue: ");
		String id = scanner.nextLine();
		boolean found = false;
		for (Book book : books) {
			if (book.getId().equals(id)) {
				if (!book.isIssued()) {
					book.setIssued(true);
					System.out.println("Book '" + book.getTitle() + "' issued successfully.");
				} else {
					System.out.println("Book '" + book.getTitle() + "' is already issued.");
				}
				found = true;
				break;
			}
		}

		if (!found) {
			System.out.println("Book with ID '" + id + "' not found.");
		}
	}

	private static void displayAvailableBooks() {
		System.out.println("--- Available Books ---");
		boolean found = false;
		for (Book book : books) {
			if (!book.isIssued()) {
				System.out.println(book);
				found = true;
			}
		}
		if (!found) {
			System.out.println("No available books at the moment.");
		}
	}

	private static void displayAllIssuedBooks() {
		System.out.println("--- All Issued Books ---");
		boolean found = false;
		for (Book book : books) {
			if (book.isIssued()) {
				System.out.println(book);
				found = true;
			}
		}
		if (!found) {
			System.out.println("No books are currently issued.");
		}
	}

	public static void returnBook() {
		System.out.print("Enter the ID of the book to return: ");
		String id = scanner.nextLine();
		boolean found = false;
		for (Book book : books) {
			if (book.getId().equals(id)) {
				if (book.isIssued()) {
					book.setIssued(false);
					System.out.println("Book '" + book.getTitle() + "' returned successfully.");
				} else {
					System.out.println("Book '" + book.getTitle() + "' was not issued.");
				}
				found = true;
				break;
			}
		}
		if (!found) {
			System.out.println("Book with ID '" + id + "' not found.");
		}
	}

	private static void sortBooksMenu() {
		System.out.println("Sort Books By:");
		System.out.println("1. Ascending Order of Author");
		System.out.println("2. Descending Order of Title");
		System.out.print("Enter your sort choice: ");
		int sortChoice = scanner.nextInt();
		scanner.nextLine();

		switch (sortChoice) {
		case 1:
			sortBooksByAuthorAscending();
			break;
		case 2:
			sortBooksByTitleDescending();
			break;
		default:
			System.out.println("Invalid sort choice.");
		}
	}

	private static void sortBooksByAuthorAscending() {
		Collections.sort(books, new Comparator<Book>() {

			public int compare(Book b1, Book b2) {
				return b1.getAuthor().compareToIgnoreCase(b2.getAuthor());
			}
		});
		System.out.println("Books sorted by Author (Ascending):");
		displayAllBooks();
	}

	private static void sortBooksByTitleDescending() {
		Collections.sort(books, new Comparator<Book>() {

			public int compare(Book b1, Book b2) {
				return b2.getTitle().compareToIgnoreCase(b1.getTitle());
			}
		});
		System.out.println("Books sorted by Title (Descending):");
		displayAllBooks();
	}

	private static void displayAllBooks() {
		if (books.isEmpty()) {
			System.out.println("No books in the collection.");
			return;
		}
		for (Book book : books) {
			System.out.println(book);
		}
	}
}
