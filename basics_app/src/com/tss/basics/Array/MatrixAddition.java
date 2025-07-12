package com.tss.basics.Array;

import java.util.Scanner;

public class MatrixAddition {

	public static void main(String[] args) {
		matricesAddition();

	}

	private static void matricesAddition() {
		Scanner scanner = new Scanner(System.in);

		int matrix1[][] = new int[3][3];
		int matrix2[][] = new int[3][3];
		int result[][] = new int[3][3];

		System.out.println("Enter the Elements of matriex1: ");
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				matrix1[i][j] = scanner.nextInt();
			}
			System.out.println();
		}

		System.out.println("Enter the Elements of matriex2: ");
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				matrix2[i][j] = scanner.nextInt();
			}
			System.out.println();
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				result[i][j] = matrix1[i][j] + matrix2[i][j];

			}
			System.out.println();
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(result[i][j] + "\t");

			}
			System.out.println();
		}
	}

}
