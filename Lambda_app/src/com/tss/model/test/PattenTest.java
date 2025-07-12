package com.tss.model.test;

import com.tss.model.IPatten;

public class PattenTest {

	public static void main(String[] args) {

		 IPatten patten1 = () -> {
	            int count = 1;
	            for (int i = 1; i <= 3; i++) {
	                for (int j = 1; j <= i; j++) {
	                    System.out.print(count + " ");
	                    count++;
	                }
	                System.out.println(); 
	            }
	        };

	        patten1.patten();
		
	}

}
