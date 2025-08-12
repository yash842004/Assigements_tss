package com.tss.tasks;

public class Consumer implements Runnable {
    Q_class q_class;

    public Consumer(Q_class q_class) {
        this.q_class = q_class;
        new Thread(this, "Consumer").start();
    }

    @Override
    public void run() {
        while (true) {
            q_class.get();
        }
    }
}
