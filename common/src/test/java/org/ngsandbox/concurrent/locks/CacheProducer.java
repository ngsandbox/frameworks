package org.ngsandbox.concurrent.locks;

import java.util.Random;
import java.util.concurrent.Callable;

class CacheProducer implements Callable<String> {

    private final Random rand = new Random();
    private final CacheWithReadWriteLock cache;

    CacheProducer(CacheWithReadWriteLock cache) {
        this.cache = cache;
    }

    public String call() {
        while (true) {
            long key = rand.nextInt(1_000);
            cache.put(key, Long.toString(key));
            if (cache.get(key) == null) {
                System.out.println("Key " + key + " has not been put in the map");
            }
        }
    }
}
