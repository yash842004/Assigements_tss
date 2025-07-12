package com.tss.Model.Test;

import java.util.Scanner;

public class MartixTest {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		int matrix[][] = new int[3][3];

		readMatrix(matrix, scanner);
		printMatrix(matrix);
	}

	private static void printMatrix(int[][] matrix) {
		for (int i = 0; i < 3; i++) {
			printRow(matrix, i);
			System.out.println();
		}
	}

	private static void printRow(int[][] matrix, int i) {
		for (int j = 0; j < 3; j++) {
			System.out.print(matrix[i][j] + "\t");

		}
	}

	private static void readMatrix(int[][] matrix, Scanner scanner) {
		System.out.println("Enter matrix Element: ");
		for (int i = 0; i < 3; i++) {
			readRow(matrix, scanner, i);
		}
	}

	private static void readRow(int[][] matrix, Scanner scanner, int i) {
		for (int j = 0; j < 3; j++) {
			matrix[i][j] = scanner.nextInt();
		}
	}

}
