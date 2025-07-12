package com.tss.basics.Array;

import java.util.Scanner;

public class SortedArraySquare {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter size of array: ");
		int sizeOfArray = scanner.nextInt();

		int[] arr = new int[sizeOfArray];
		System.out.println("Enter " + sizeOfArray + " sorted elements:");

		for (int i = 0; i < sizeOfArray; i++) {
			arr[i] = scanner.nextInt();
		}

		int[] result = getSortedSquares(arr);

		System.out.print("Sorted Squared Array: ");
		for (int i = 0; i < result.length; i++) {
			System.out.print(result[i] + " ");
		}
	}

	public static int[] getSortedSquares(int[] arr) {
		int arrayLength = arr.length;
		int[] result = new int[arrayLength];
		int left = 0;
		int right = arrayLength - 1;
		int index = arrayLength - 1;

		while (left <= right) {
			int leftSq = arr[left] * arr[left];
			int rightSq = arr[right] * arr[right];

			if (leftSq > rightSq) {
				result[index] = leftSq;
				left++;
			} else {
				result[index] = rightSq;
				right--;
			}
			index--;
		}

		return result;
	}
}
