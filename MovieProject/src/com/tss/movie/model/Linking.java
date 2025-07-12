package com.tss.movie.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class Linking {

	public static void main(String[] args) {

		LinkedList<String> nameChain = new LinkedList<>();
		nameChain.add("Deep");
		nameChain.add("Mahek");
		nameChain.add("Yash");
		nameChain.add("Rishit");
		nameChain.add("Priyank");

		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter a name to see the chain: ");
		String startName = scanner.nextLine();

		printChain(nameChain, startName);

		scanner.close();
	}

	public static void printChain(LinkedList<String> chainList, String startName) {
		int startIndex = -1;

		Iterator<String> iterator = chainList.iterator();
		int currentIndex = 0;
		while (iterator.hasNext()) {
			String name = iterator.next();
			if (name.equalsIgnoreCase(startName)) {
				startIndex = currentIndex;
				break;
			}
			currentIndex++;
		}

		if (startIndex == -1) {
			System.out.println("Name '" + startName + "' not found in the chain.");
			return;
		}

		StringBuilder chainOutput = new StringBuilder();
		for (int i = startIndex; i < chainList.size(); i++) {
			chainOutput.append(chainList.get(i));
			if (i < chainList.size() - 1) {
				chainOutput.append(" -> ");
			}
		}
		System.out.println(chainOutput.toString());
	}

}
