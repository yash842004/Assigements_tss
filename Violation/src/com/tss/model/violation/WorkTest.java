package com.tss.model.violation;

public class WorkTest {

	public static void main(String[] args) {

		IWorker worker = new Labour();
		worker.drink();
		worker.eat();
		worker.startWork();
		worker.stopWork();
		IWorker robot = new Labour();
		robot.drink();
		robot.eat();
		robot.startWork();
		robot.stopWork();

		
		
	}

}
