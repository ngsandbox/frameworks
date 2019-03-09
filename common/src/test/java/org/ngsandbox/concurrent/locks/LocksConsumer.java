package org.ngsandbox.concurrent.locks;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

class LocksConsumer implements Callable<String> {

    private final List<Integer> buffer;
    private final Lock lock;
    private final Condition isEmpty;
    private final Condition isFull;

    LocksConsumer(List<Integer> buffer, Lock lock, Condition isEmpty, Condition isFull) {
        this.buffer = buffer;
        this.lock = lock;
        this.isEmpty = isEmpty;
        this.isFull = isFull;
    }


    public String call() throws InterruptedException, TimeoutException {
        int count = 0;
        while (count++ < 50) {
            try {
                lock.lock();
                while (buffer.isEmpty()) {
                    // wait
                    if (!isEmpty.await(10, TimeUnit.MILLISECONDS)) {
                        throw new TimeoutException("Consumer time out");
                    }
                }
                buffer.remove(buffer.size() - 1);
                // signal
                isFull.signalAll();
            } finally {
                lock.unlock();
            }
        }
        return "Consumed " + (count - 1);
    }
}
