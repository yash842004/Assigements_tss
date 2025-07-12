package collection_app.com.tss.model;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class SetTest {

	public static void main(String[] args) {

		Set<Integer> numbers = new HashSet<>(); // it will add value in unordered way.

		numbers.add(10);
		numbers.add(30);
		numbers.add(50);
		numbers.add(1);

		numbers.remove(1);

		System.out.println(numbers.size());

		System.out.println(numbers);

		Set<Integer> number = new LinkedHashSet<>(); // it use doubly linked list so input and output is in order.

		number.add(1);
		number.add(2);
		number.add(3);
		number.add(44);
		number.add(33);

		System.out.println(number);

		Set<Integer> treeNumber = new TreeSet<>(); // sorted order output.

		treeNumber.add(11);
		treeNumber.add(32);
		treeNumber.add(43);
		treeNumber.add(1);

		System.out.println(treeNumber);

	}

}