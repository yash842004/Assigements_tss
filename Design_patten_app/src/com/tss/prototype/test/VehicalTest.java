package com.tss.prototype.test;

import com.tss.prototype.Bike;
import com.tss.prototype.Car;
import com.tss.prototype.Engine;

public class VehicalTest {

	public static void main(String[] args) {

		Engine v8 = new Engine("super charge", 2000);
		Car BMW = new Car("x200", "black", v8, "suv");
		BMW.drive();

		Bike honda = new Bike("cd-120", "black", v8, "normal");
		honda.drive();
	}

}
