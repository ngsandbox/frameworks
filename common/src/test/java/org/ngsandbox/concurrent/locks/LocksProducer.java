package org.ngsandbox.concurrent.locks;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class LocksProducer implements Callable<String> {

    private final List<Integer> buffer;
    private final Lock lock;
    private final Condition isEmpty;
    private final Condition isFull;

    LocksProducer(List<Integer> buffer, Lock lock, Condition isEmpty, Condition isFull) {
        this.buffer = buffer;
        this.lock = lock;
        this.isEmpty = isEmpty;
        this.isFull = isFull;
    }


    public String call() throws InterruptedException {
        int count = 0;
        while (count++ < 50) {
            try {
                lock.lock();
                int i = 10 / 0;
                while (buffer.size() == 10) {
                    // wait
                    isFull.await();
                }
                buffer.add(1);
                // signal
                isEmpty.signalAll();
            } finally {
                lock.unlock();
            }
        }
        return "Produced " + (count - 1);
    }
}