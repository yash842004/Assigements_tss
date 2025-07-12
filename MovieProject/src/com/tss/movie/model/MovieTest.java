package com.tss.movie.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class MovieTest {

	private static final int MAX_MOVIES = 5;
	private static final String DATA_FILE = "movies.ser";
	private List<Movie> movies;
	private Scanner scanner;

	public MovieTest() {
		this.movies = new ArrayList<>();
		this.scanner = new Scanner(System.in);
		loadMovies();
	}

	@SuppressWarnings("unchecked")
	private void loadMovies() {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
			movies = (List<Movie>) ois.readObject();
			System.out.println("Movies loaded from " + DATA_FILE);
		} catch (FileNotFoundException e) {
			System.out.println("No existing movie data found. Starting with an empty collection.");
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error loading movies: " + e.getMessage());

			movies = new ArrayList<>();
		}
	}

	private void saveMovies() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
			oos.writeObject(movies);
			System.out.println("Movies saved to " + DATA_FILE);
		} catch (IOException e) {
			System.err.println("Error saving movies: " + e.getMessage());
		}
	}

	public void displayMovies() {
		if (movies.isEmpty()) {
			System.out.println("0th Movie store is empty");
		} else {
			System.out.println("\n--- Current Movies ---");
			for (int i = 0; i < movies.size(); i++) {
				System.out.println((i + 1) + ". " + movies.get(i));
			}
			System.out.println("----------------------");
		}
	}

	public void addMovie() {
		if (movies.size() >= MAX_MOVIES) {
			System.out.println("6th Movie store already full");
			return;
		}

		System.out.println("\nEnter movie details:");
		System.out.print("Name: ");
		String name = scanner.nextLine().trim();
		if (name.isEmpty()) {
			System.out.println("Movie name cannot be empty. Aborting add.");
			return;
		}

		System.out.print("Genre: ");
		String genre = scanner.nextLine().trim();
		if (genre.isEmpty()) {
			System.out.println("Movie genre cannot be empty. Aborting add.");
			return;
		}

		int year = 0;
		boolean validYear = false;
		while (!validYear) {
			System.out.print("Year: ");
			try {
				year = Integer.parseInt(scanner.nextLine());
				if (String.valueOf(year).length() != 4) {
					System.out.println("Please enter a 4-digit year.");
				} else {
					validYear = true;
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid year. Please enter a number.");
			}
		}

		String generatedId = generateMovieId(name, genre, year);

		Movie newMovie = new Movie(generatedId, name, genre, year);
		movies.add(newMovie);
		saveMovies();
		System.out.println("Movie '" + name + "' added successfully with ID: " + generatedId + "!");
	}

	public void deleteMovieById() {
		if (movies.isEmpty()) {
			System.out.println("No movies to delete. The store is empty.");
			return;
		}

		System.out.print("Enter the ID of the movie to delete: ");
		String idToDelete = scanner.nextLine().trim();

		boolean found = false;
		Iterator<Movie> iterator = movies.iterator();
		while (iterator.hasNext()) {
			Movie movie = iterator.next();
			if (movie.getMovieId().equalsIgnoreCase(idToDelete)) {
				iterator.remove();
				found = true;
				System.out.println("Movie with ID '" + idToDelete + "' deleted successfully.");
				saveMovies();
				break;
			}
		}

		if (!found) {
			System.out.println("Movie with ID '" + idToDelete + "' not found.");
		}
	}

	private String generateMovieId(String name, String genre, int year) {
		String namePart = name.length() >= 2 ? name.substring(0, 2) : (name + "XX").substring(0, 2);
		String genrePart = genre.length() >= 2 ? genre.substring(0, 2) : (genre + "XX").substring(0, 2);
		String yearPart = String.valueOf(year % 100);
		if (yearPart.length() == 1) {
			yearPart = "0" + yearPart;
		}

		return (namePart + genrePart + yearPart).toUpperCase();
	}

	public void clearAllMovies() {
		System.out.print("Are you sure you want to clear all movies? (yes/no): ");
		String confirm = scanner.nextLine().toLowerCase();
		if (confirm.equals("yes")) {
			movies.clear();
			saveMovies();
			System.out.println("All movies cleared.");
		} else {
			System.out.println("Operation cancelled.");
		}
	}

	public void showMenu() {
		System.out.println("\n--- Simple Movies App Menu ---");
		System.out.println("1. Display movies");
		System.out.println("2. Add movies");
		System.out.println("3. Clear all");
		System.out.println("4. Delete movie by ID");
		System.out.println("5. Exit");
		System.out.print("Enter your choice: ");
	}

	public void run() {
		int choice;
		do {
			showMenu();
			try {
				choice = Integer.parseInt(scanner.nextLine());
				switch (choice) {
				case 1:
					displayMovies();
					break;
				case 2:
					addMovie();
					break;
				case 3:
					clearAllMovies();
					break;
				case 4:
					deleteMovieById();
					break;
				case 5:
					System.out.println("Exiting Simple Movies App. Your data has been saved.");
					break;

				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number.");
				choice = 0;
			}
		} while (choice != 5);

		scanner.close();
	}

	public static void main(String[] args) {
		MovieTest app = new MovieTest();
		app.run();
	}
}