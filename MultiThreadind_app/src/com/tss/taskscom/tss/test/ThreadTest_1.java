package com.tss.taskscom.tss.test;

import com.tss.tasks.MyTask;
import com.tss.tasks.MyTask2;

public class ThreadTest_1 {

	public static void main(String[] args) {


		Thread thread1 = new Thread(new MyTask());
		thread1.start();
		Thread thread2 = new Thread(new MyTask2());
		thread2.start();
		Thread thread3 = new Thread(new MyTask());
		thread3.start();
		Thread thread4 = new Thread(new MyTask());
		thread4.start();
		
		Runnable task5 = () -> {
			System.out.println("printing");
		};
		Thread thread5 = new Thread(task5);
	}

}
