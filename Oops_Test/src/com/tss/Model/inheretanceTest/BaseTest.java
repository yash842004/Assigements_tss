package com.tss.Model.inheretanceTest;

import com.tss.Model.inheretance.model.Base;
import com.tss.Model.inheretance.model.Drived1;
import com.tss.Model.inheretance.model.Drived2;

public class BaseTest {

	public static void main(String[] args) {


		Base base1 = new Drived1();
		base1.display();
		Base base2 = new Drived2();
		base2.display();


		
	}

}
