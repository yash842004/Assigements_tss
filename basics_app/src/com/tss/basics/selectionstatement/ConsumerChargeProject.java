package com.tss.basics.selectionstatement;

import java.util.Scanner;

public class ConsumerChargeProject {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		 Double meter_charge = 75.0;
		 Double Total_water_bill = 0.0;
		 System.out.println("Enter the charge ");
		  Double unit = sc.nextDouble();
		  Double charge = 0.0;
		  Double temp = unit; // 400
		  
		  
		  if (unit <= 100) {
			  
			  
			  charge = unit * 5;
		  }
		  else if (unit <= 250 && unit > 100) {
			  charge = unit * 10;
			  
		  }
		  else {
			  charge = unit * 20;
			  
		  }
		  
		  Total_water_bill = meter_charge + charge;
		  System.out.println("total bill "+Total_water_bill );
		  
		
		  
	}

}
