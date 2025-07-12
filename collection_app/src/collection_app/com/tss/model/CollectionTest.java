package collection_app.com.tss.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class CollectionTest {

	@SuppressWarnings("unlikely-arg-type")
	public static void main(String[] args) {

		ArrayList<String> names = new ArrayList();
		String name = "yash";

		names.add("om");
		names.add("raj");
		names.add("rohal");

		System.out.println(names);

		names.add(1, "oma");
		names.set(0, "yash");

		System.out.println(names);

//		for(String object:names) {
//			System.out.println(object);
//		}

//		for(int i =0; i < names.size();i++) {
//			System.out.println(names.get(i));
//		}
		System.out.println(names.contains(name));

//		Iterator iteroter = names.iterator();
//		while (iteroter.hasNext()) {
//			System.out.println(iteroter.next());
//		}

//		ListIterator Iiteroter = names.listIterator();
//		while (Iiteroter.hasNext()) {
//
//			System.out.println(Iiteroter.next());
//
//		}
//		while (Iiteroter.hasPrevious()) {
//
//			System.out.println(Iiteroter.previous());
//
//		}

		names.addLast("last");
		int lastIndexIs = names.lastIndexOf("last");

		names.add("last");
		System.out.println(names);
		System.out.println(lastIndexIs);

	}

}
