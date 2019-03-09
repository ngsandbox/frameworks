package org.ngsandbox.concurrent.executors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class PlayingWithCallablesAndFutures {

    @Test
    void testCallablesAndFutures() {

        Callable<String> task = () -> {
            throw new IllegalStateException("I throw an exception in thread " + Thread.currentThread().getName());
        };

        ExecutorService executor = Executors.newFixedThreadPool(4);

        try {
            for (int i = 0; i < 10; i++) {
                Future<String> future = executor.submit(task);
                Assertions.assertThrows(ExecutionException.class,
                        () -> System.out.println("I get: " + future.get()));
            }
        } finally {
            executor.shutdown();
        }
    }

    @Test
    void testRunnable() throws ExecutionException, InterruptedException {

        AtomicInteger integer = new AtomicInteger();
        Runnable task = () -> System.out.println("I am in thread " + Thread.currentThread().getName() +
                " counter " + integer.incrementAndGet());

        // ExecutorService service = Executors.newSingleThreadExecutor();
        ExecutorService service = null;
        try {
            service = Executors.newFixedThreadPool(4);
            List<CompletableFuture> futures = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                futures.add(CompletableFuture.runAsync(task, service));
            }

            CompletableFuture
                    .allOf(futures.toArray(new CompletableFuture[0]))
                    .get();
        } finally {
            service.shutdown();
        }


        Assertions.assertEquals(10, integer.get());
    }
}
