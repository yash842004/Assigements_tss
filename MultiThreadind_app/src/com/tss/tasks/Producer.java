package com.tss.tasks;

public class Producer implements Runnable {
    Q_class q_class;

    public Producer(Q_class q_class) {
        this.q_class = q_class;
        new Thread(this, "Producer").start();
    }

    @Override
    public void run() {
        int i = 0;
        while (true) {
            q_class.put(i++);
        }
    }
}
