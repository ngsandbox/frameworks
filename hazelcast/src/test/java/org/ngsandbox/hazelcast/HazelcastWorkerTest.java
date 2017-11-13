package org.ngsandbox.hazelcast;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.ngsandbox.common.Worker;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


class HazelcastWorkerTest  {

    private static final Logger LOG = LoggerFactory.getLogger(HazelcastWorkerTest .class);
    private int QTY = 20;
    private int WORKERS_LIMIT = 10;

    private String znode = "/worker_node";

    @Test
    void testWorkers() throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(QTY);
        final HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
        try {
            for (int i = 0; i < 200; ++i) {
                final int finalI = i;
                Callable<Void> task = () -> {
                    LOG.trace("Started worker " + finalI);
                    try (Worker worker = new HazelcastWorkerImpl(hazelcastInstance)){
                        if(finalI > 10 && finalI % 10 == 0){
                            LOG.trace("Waiting hazelcast workers limit for thread " + finalI);
                            worker.waitTill(WORKERS_LIMIT);

                            LOG.trace("Starting workers for thread " + finalI);
                            worker.start();
                        }
                    }

                    return null;
                };

                service.submit(task);
            }

            Thread.sleep(10000);
        }
        finally {
            hazelcastInstance.shutdown();
            service.shutdown();
            service.awaitTermination(60, TimeUnit.SECONDS);
        }
    }
}
