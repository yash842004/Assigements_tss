package collection_app.com.tss.model;

public class Comparable01 implements Comparable<Employee> {

	private int employeeId;

	@Override
	public int compareTo(Employee o) {

		return Integer.compare(this.employeeId, employeeId);

	}

}
