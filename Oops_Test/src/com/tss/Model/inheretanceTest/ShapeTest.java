package com.tss.Model.inheretanceTest;

import com.tss.Model.inheretance.model.Circle;
import com.tss.Model.inheretance.model.Rectangle;
import com.tss.Model.inheretance.model.Shape;



public class ShapeTest {

	public static void main(String[] args) {
		Shape circle = new Circle(10);
		circle.area();
		Shape rectangle = new Rectangle(10, 10);
		rectangle.area();

	}

}
