package com.tss.tasks;

public class MyTask implements Runnable {
	
	public void run() {
		for(int i = 5; i >= 0; i--) {
			System.out.println(Thread.currentThread().getName()+" : "+i);
			
		}
		
	}

}
