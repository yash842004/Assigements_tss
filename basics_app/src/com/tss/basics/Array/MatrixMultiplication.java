package com.tss.basics.Array;

import java.util.Scanner;

public class MatrixMultiplication {

	public static void main(String[] args) {

		int matrices1[][] = new int[3][3];
		int matrices2[][] = new int[3][3];
		int result[][] = new int[3][3];

		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the Elements of matrices1:");
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				matrices1[i][j] = scanner.nextInt();
			}
			System.out.println();
		}

		System.out.println("Enter the Elements of matrices2:");
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				matrices2[i][j] = scanner.nextInt();
			}
			System.out.println();
		}
		
		
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				for(int k = 0; k < 3; k++) {
					
					result[i][j] += matrices1[i][k] * matrices2[k][j];
				}
			}
		}
		
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				System.out.println(result[i][j]);
			}
			System.out.println();
		}

	}

}
