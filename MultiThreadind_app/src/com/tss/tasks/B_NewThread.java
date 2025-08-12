package com.tss.tasks;

public class B_NewThread implements Runnable {

	String name;
	public Thread t;

	public B_NewThread(String threadname) {
		name = threadname;
		t = new Thread(this, name);
		System.out.println("New thread: " + t);
		t.start();
	}

	@Override
	public void run() {

		try {
			for (int i = 5; i > 0; i--) {
				System.out.println(this.name + ": " + i);
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			System.out.println(name + " interrupted.");
		}
		System.out.println(name + " exiting.");

	}

}
