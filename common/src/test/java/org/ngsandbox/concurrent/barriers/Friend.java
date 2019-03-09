package org.ngsandbox.concurrent.barriers;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

class Friend implements Callable<String> {

    private CyclicBarrier barrier;

    public Friend(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    public String call() throws Exception {

        try {
            //Random random = new Random();
            //Thread.sleep((random.nextInt(20)*100 + 100));
            System.out.println("I just arrived, waiting for the others...");

            barrier.await();

            System.out.println("Let's go to the cinema!");
            return "ok";
        } catch(InterruptedException e) {
            System.out.println("Interrupted");
        }
        return "nok";
    }
}
