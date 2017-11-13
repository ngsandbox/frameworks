package org.ngsandbox.hazelcast;

import com.hazelcast.core.*;
import org.ngsandbox.common.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HazelcastWorkerImpl implements Worker {

    private static final Logger LOG = LoggerFactory.getLogger(HazelcastWorkerImpl.class);

    private boolean newInstance = false;
    private final HazelcastInstance hz;
    private final IAtomicLong counter;
    private final ILock lock;
    private final ICondition iCondition;

    public HazelcastWorkerImpl() {
        this(Hazelcast.newHazelcastInstance());
        newInstance = true;
    }

    public HazelcastWorkerImpl(HazelcastInstance hz) {
        newInstance = false;
        this.hz = hz;
        counter = hz.getAtomicLong("counter");
        lock = hz.getLock("lock");
        iCondition = lock.newCondition("one");
    }

    @Override
    public void waitTill(int countWorkers) {
        lock.lock();
        try {
            long count = counter.get();
            if (count != -1) {
                count = counter.incrementAndGet();
                LOG.trace("Current count " + count);

                if (count == countWorkers) {
                    System.out.println("\nWe are started!");
                    counter.set(-1);
                    iCondition.signalAll();
                } else {
                    while (count != -1) {
                        LOG.trace(" Waiting");
                        try {
                            iCondition.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        count = counter.get();
                    }
                }
            } else {
                LOG.warn(" The group has been already started");
            }
        } finally {
            lock.unlock();
            LOG.trace("Released");
        }
    }

    @Override
    public void close() throws Exception {
        if (newInstance) {
            LOG.trace("Shutdown");
            hz.shutdown();
        }
    }
}
