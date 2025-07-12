package com.tss.basics.Array;

public class BubbleSort {

	public static void main(String[] args) {
		boolean swap;

		int[] number = new int[5];
		number[0] = 43;
		number[1] = 34;
		number[2] = 75;
		number[3] = 11;
		number[4] = 90;

		for (int i = 0; i <= number.length; i++) {
			swap = false;

			for (int j = 1; j < number.length - i; i++) {
				if (number[j] < number[j - 1]) {
					int temp = number[j];
					number[j] = number[j - 1];
					number[j - 1] = temp;
					swap = true;

				}

			}
			if (!swap) {
				break;
			}

		}

	}

}
