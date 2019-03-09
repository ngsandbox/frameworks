package org.ngsandbox.concurrent.atomiccounter;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class TestAtomicCounter {


	private static MyAtomicCounter counter = new MyAtomicCounter(0);

	@Test
	void testAtomic() {

		class Incrementer implements Runnable {
			
			public void run() {
				for (int i = 0 ; i < 1_000 ; i++) {
					counter.myIncrementAndGet();
				}
			}
		}
		
		class Decrementer implements Runnable {
			
			public void run() {
				for (int i = 0 ; i < 1_000 ; i++) {
					counter.decrementAndGet();
				}
			}
		}
		
		ExecutorService executorService = Executors.newFixedThreadPool(8);
		List<Future<?>> futures = new ArrayList<>();
		
		try {
				
			for (int i = 0 ; i < 4 ; i++) {
				futures.add(executorService.submit(new Incrementer()));
			}
			for (int i = 0 ; i < 4 ; i++) {
				futures.add(executorService.submit(new Decrementer()));
			}
			
			futures.forEach(
				future -> {
					try {
						future.get();
					} catch (InterruptedException | ExecutionException e) {
						System.out.println(e.getMessage());
					}
				}
			);
			
			System.out.println("counter = " + counter);
			System.out.println("# increments = " + counter.getIncrements());
			
		} finally {
			executorService.shutdown();
		}
	}
}
