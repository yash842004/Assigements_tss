package com.tss.taskscom.tss.test;

import com.tss.tasks.B_NewThread;

public class B_Test1 {

	public static void main(String[] args) {

		B_NewThread ob1 = new B_NewThread("One");
		B_NewThread ob2 = new B_NewThread("Two");
		B_NewThread ob3 = new B_NewThread("Three");

		System.out.println("Thread One is alive: " + ob1.t.isAlive());
		System.out.println("Thread Two is alive: " + ob2.t.isAlive());
		System.out.println("Thread Three is alive: " + ob3.t.isAlive());

		try {
			System.out.println("Waiting for threads to finish.");
			ob1.t.join();
			ob2.t.join();
			ob3.t.join();
		} catch (InterruptedException e) {
			System.out.println("Main thread Interrupted");
		}

		System.out.println("Thread One is alive: " + ob1.t.isAlive());
		System.out.println("Thread Two is alive: " + ob2.t.isAlive());
		System.out.println("Thread Three is alive: " + ob3.t.isAlive());

		System.out.println("Main thread exiting.");
	}

}
