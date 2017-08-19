package org.frameworks.zookeeper;

import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.zookeeper.CreateMode;
import org.frameworks.common.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZooWorker implements Worker {

    private static final Logger LOG = LoggerFactory.getLogger(ZooWorker.class);

    private final ZooConfigMonitor<Integer> zooMonitor;
    private final InterProcessMutex mutex;
    private final DistributedBarrier barrier;
    private final SharedCount counter;

    public ZooWorker(String connectString) throws Exception {
        zooMonitor = new ZooConfigMonitor<>(connectString, "/zoo-workers", Integer.class, CreateMode.EPHEMERAL);
        mutex = zooMonitor.getMutex();
        barrier = zooMonitor.getBarrier();
        counter = zooMonitor.getSharedCount();
    }

    @Override
    public void waitTill(int countWorkers) throws Exception {
        barrier.setBarrier();
        counter.start();
        mutex.acquire();
        int count = compateOrInc(countWorkers);
        while (count != -1) {
            LOG.debug(" Waiting");
            try {
                barrier.waitOnBarrier();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            count = counter.getCount();
        }

        barrier.removeBarrier();
    }

    private int compateOrInc(int countWorkers) throws Exception {
        int count;
        try {
            count = counter.getCount();
            if (count != -1) {
                if (count == countWorkers - 1) {
                    LOG.info("We are started!");
                    System.out.println("We are started!");
                    count = -1;
                    barrier.removeBarrier();
                } else {
                    count = count + 1;
                }

                LOG.debug("Current count " + count);
                counter.setCount(count);
            }

        } finally {
            mutex.release();
        }
        return count;
    }

    @Override
    public void close() throws Exception {
        zooMonitor.close();
    }
}
