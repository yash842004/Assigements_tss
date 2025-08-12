package com.tss.taskscom.tss.test;

import com.tss.tasks.Callme;

public class Caller implements Runnable {
	String msg;
	Callme target;
	Thread t;

	public Caller(Callme targ, String s) {
		target = targ;
		msg = s;
		t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		synchronized (target) { 
			target.call(msg);
		}
	}

}
