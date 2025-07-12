package com.tss.model.test;

import java.util.function.Function;
import java.util.function.Supplier;

import com.tss.model.BillGenerator;

public class BillGeneratorTest {

	public static void main(String[] args) {

		
		 
		
		Supplier<BillGenerator> billSupplier = () -> new BillGenerator("keyboard",1000);
		
		
		Function<BillGenerator, String> billGenerate = bill -> bill.generateBill();
			
			String result = billGenerate.apply(bill);
			
			
		};
		
	}

}
