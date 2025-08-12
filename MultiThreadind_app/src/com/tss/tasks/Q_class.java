package com.tss.tasks;

public class Q_class {
    int n;

    public synchronized int get() {
        System.out.println("Got: " + n);
        return n;
    }

    public synchronized void put(int n) {
        this.n = n;
        System.out.println("Put: " + n);
    }
}
