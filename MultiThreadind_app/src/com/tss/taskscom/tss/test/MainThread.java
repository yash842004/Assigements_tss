package com.tss.taskscom.tss.test;

import com.tss.tasks.MyThread;

public class MainThread {

	public static void main(String[] args) {
//		Thread.currentThread().setName("Royal");
//		Thread.currentThread().setPriority(1);
//		System.out.println(Thread.currentThread());
//		System.out.println("start");
		try {
			Thread.currentThread().sleep((long) 5000.00);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("sleeped thread wake Up!");
		System.out.println(Thread.currentThread());

		MyThread thread_1 = new MyThread("thread1");
		thread_1.setPriority(1);
		thread_1.start();
		MyThread thread_2 = new MyThread("thread2");
		thread_2.setPriority(5);
		thread_2.start();
		MyThread thread_3 = new MyThread("thread3");
		thread_3.setPriority(9);
		thread_3.start();

	}

}
