package com.tss.test;

import com.tss.tasks.Consumer;
import com.tss.tasks.Producer;
import com.tss.tasks.Q_class;

public class Test_PC {
    public static void main(String[] args) {
        Q_class q_class = new Q_class();
        new Producer(q_class);
        new Consumer(q_class);
        
    }
}
