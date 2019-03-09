package org.ngsandbox.concurrent.locks;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheWithReadWriteLock {

	private Map<Long, String> cache = new HashMap<>();
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	private Lock readLock = lock.readLock();
	private Lock writeLock = lock.writeLock();

	public String put(Long key, String value) {
		writeLock.lock();
		try {
			return cache.put(key, value);
		} finally {
			writeLock.unlock();
		}
	}
	
	public String get(Long key) {
		readLock.lock();
		try {
			return cache.get(key);
		} finally {
			readLock.unlock();
		}
	}

	@Test
	void testReadWriteLock() {

		CacheWithReadWriteLock cache = new CacheWithReadWriteLock();

		ExecutorService executorService = Executors.newFixedThreadPool(4);

		System.out.println("Adding value...");
		try  {
			for (int i = 0; i < 4; i++) {
				executorService.submit(new CacheProducer(cache));
			}
		} finally {
			executorService.shutdown();
		}
	}
}
