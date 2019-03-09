package org.ngsandbox.concurrent.barriers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TestBarrierInAction {


    @Test
    public void testBarrier() {
        ExecutorService executorService = Executors.newFixedThreadPool(6);

        CyclicBarrier barrier = new CyclicBarrier(4, () -> System.out.println("Barrier openning"));
        List<Future<String>> futures = new ArrayList<>();

        int counter = 0;
        try {
            for (int i = 0; i < 4; i++) {
                futures.add(executorService.submit(new Friend(barrier)));
            }


            for (Future<String> future : futures) {
                try {
                    future.get(200, TimeUnit.MILLISECONDS);
                    counter++;
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println(e.getMessage());
                } catch (TimeoutException e) {
                    System.out.println("Timed out");
                    future.cancel(true);
                }
            }

        } finally {
            executorService.shutdown();
        }
        Assertions.assertThat(counter).isEqualTo(4);
    }
}
