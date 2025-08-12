package com.tss.tasks;

public class MyThread extends Thread {
	
	public MyThread(String name){
		super(name);
	}
	public void run() {
		for(int i = 5; i >= 0; i--) {
			System.out.println(Thread.currentThread().getName()+" : "+i);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	
	}

}
