package com.tss.model.Test;

import java.util.Arrays;
import java.util.Comparator;

public class ListOfNameTest {

	public static void main(String[] args) {
		 String[] names = {"Jay", "Nimesh", "Mark", "Mahesh", "Ramesh"};

	        System.out.println("a. First 3 students sorted ascending:");
	        Arrays.stream(names)
	              .limit(3)
	              .sorted()
	              .forEach(System.out::println);
	        System.out.println();
	        System.out.println("b. First 3 students sorted ascending with 'a':");
	        Arrays.stream(names)
	              .limit(3)
	              .filter(n -> n.toLowerCase().contains("a"))
	              .sorted()
	              .forEach(System.out::println);
	        System.out.println();
	        System.out.println("c. Students sorted descending:");
	        Arrays.stream(names)
	              .sorted(Comparator.reverseOrder())
	              .forEach(System.out::println);
	        System.out.println();
	        System.out.println("d. First 3 characters of each name:");
	        Arrays.stream(names)
	              .map(name -> name.length() >= 3 ? name.substring(0,3) : name)
	              .forEach(System.out::println);
	        System.out.println();
	        System.out.println("e. Names with <= 4 characters:");
	        Arrays.stream(names)
	              .filter(n -> n.length() <= 4)
	              .forEach(System.out::println);
	}

}
