package com.tss.tasks;

public class MyTask2 implements Runnable {

	@Override
	public void run() {

		for(int i = 0; i < 5; i++) {
			System.out.println("starting multithreading");
		}
	}

}
