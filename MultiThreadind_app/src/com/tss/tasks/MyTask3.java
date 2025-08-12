package com.tss.tasks;

import java.util.Random;
import java.util.concurrent.Callable;

public class MyTask3 implements Callable<Integer>{

	@Override
	public Integer call() throws Exception {

		Random ramdom = new Random();
		
		return ramdom.nextInt();
	}
	
	
	
}
