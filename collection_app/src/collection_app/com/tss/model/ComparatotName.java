package collection_app.com.tss.model;

import java.util.Comparator;

public class ComparatotName {

	Comparator<Employee> comparatorNameAsc = new Comparator<Employee>() {

		public int compare(Employee emp1, Employee emp2) {
			return emp1.getName().compareTo(emp2.getName());
		}
	};

}
