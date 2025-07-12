package collection_app.com.tss.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EmployeeTest {

	public int readEmployees(List<Employee> employees) {
		if (employees == null) {
			return 0;
		}
		return employees.size();
	}

	public static void main(String[] args) {

		
		List<Employee> employees = new ArrayList();
		
		employees.add(new Employee(10,"om",10000));
		System.out.println(employees);



	Comparator comparator = null;
	Collections.sort(employees,comparator);


}
}