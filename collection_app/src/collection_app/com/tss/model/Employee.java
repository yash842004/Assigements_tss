package collection_app.com.tss.model;

import java.util.Scanner;

public class Employee {
	@Override
	public String toString() {
		return "Employee [employeeId=" + employeeId + ", name=" + name + ", salary=" + salary + "]";
	}

	private int employeeId;
	private String name;
	private int salary;

	public Employee(int employeeId, String name, int salary) {
		super();
		this.employeeId = employeeId;
		this.name = name;
		this.salary = salary;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public void EnterTheDetail() {
		
	Scanner scanner = new Scanner(System.in);
	System.out.println("Enter the numebr of employes ");
	int number = scanner.nextInt();
	for(int i = 0; i > number; i++)
	{
	
	System.out.println("Enter the id: ");
	int id = scanner.nextInt();
	 setEmployeeId(id);
	 
	 System.out.println("Enter the name: ");
	 String name = scanner.next();
	 setName(name);
	 
	 System.out.println("Enter the salary: ");
	 int salary = scanner.nextInt();
	 setSalary(salary);
	}
	
	 
	}

}
