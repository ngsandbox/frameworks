package org.frameworks.zookeeper.test;

import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.frameworks.zookeeper.ZooConfigMonitor;
import org.frameworks.zookeeper.ZooWorker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@DisplayName("Zookeeper config test")
class ZooWorkerTest extends BaseZooTest {

    private static final Logger LOG = LoggerFactory.getLogger(ZooWorkerTest.class);
    private int QTY = 20;
    private int WORKERS_LIMIT = 10;

    private String znode = "/worker_node";

    @Test
    void testWorkers() throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(QTY);
        final String connectString = getConnectString();
        for (int i = 0; i < 200; ++i) {
            final int finalI = i;
            Callable<Void> task = () -> {
                LOG.trace("Started worker " + finalI);
                try (ZooWorker worker = new ZooWorker(connectString)){
                    if(finalI > 10 && finalI % 10 == 0){
                        LOG.trace("Waiting workers limit for thread " + finalI);
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

        service.shutdown();
        service.awaitTermination(60, TimeUnit.SECONDS);
    }
}
