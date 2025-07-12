package collection_app.com.tss.model;

import java.io.Serializable;

public class Person implements Serializable {

	private String name;
	private int age;
	private String address;
	private transient String password;
	private static String company = "ABC group";

	public Person(String name, int age, String address, String password) {
		super();
		this.name = name;
		this.age = age;
		this.address = address;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public String getAddress() {
		return address;
	}

	public String getPassword() {
		return password;
	}

	public String getCompany() {
		return company;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + ", address=" + address + ", password=" + password + ", compny="
				+ company + "]";
	}

}
