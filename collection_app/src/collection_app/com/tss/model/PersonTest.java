package collection_app.com.tss.model;

import java.io.*;

public class PersonTest {

	private static final String fileName = "person.txt";

	public static void main(String[] args) {

		System.out.println("Serilization");
		Person person = new Person("om", 20, "puna", "123");
		System.out.println("details -> " + person);

		try (FileOutputStream fileOut = new FileOutputStream("person.txt");
				ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
			out.writeObject(person);
			System.out.println("Person object serialized successfully to " + fileName);

		} catch (IOException i) {
			i.printStackTrace();

		}

		System.out.println("Deserilization");
		Person deserializedPerson = null;

		try (FileInputStream fileIn = new FileInputStream(fileName);
				ObjectInputStream in = new ObjectInputStream(fileIn)) {

			deserializedPerson = (Person) in.readObject();
			System.out.println("Person object deserialized successfully from " + fileName);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println("Deserialized Person: " + deserializedPerson);
		System.out.println("Deserialized Person Name: " + deserializedPerson.getName());
		System.out.println("Deserialized Person Age: " + deserializedPerson.getAge());
		System.out.println("Deserialized Person Address: " + deserializedPerson.getAddress());
		System.out.println("Deserialized Person Password : " + deserializedPerson.getPassword());
		System.out.println("Deserialized Person Company : " + deserializedPerson.getCompany());

		System.out.println("\n--- Verification ---");
		System.out.println("Is password null? " + (deserializedPerson.getPassword() == null));
		System.out.println("Is company 'xyz'? " + (deserializedPerson.getCompany().equals("xyz")));

	}

}
