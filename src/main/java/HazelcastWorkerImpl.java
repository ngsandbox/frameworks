import com.hazelcast.core.*;

public class HazelcastWorkerImpl implements Worker {

    private final HazelcastInstance hz;
    private final IAtomicLong counter;
    private final ILock lock;
    private final ICondition iCondition;

    public HazelcastWorkerImpl() {
        hz = Hazelcast.newHazelcastInstance();
        counter = hz.getAtomicLong("counter");
        lock = hz.getLock("lock");
        iCondition = lock.newCondition("one");
    }

    @Override
    public void waitTill(int countWorkers) {
        lock.lock();
        try {
            long count = counter.incrementAndGet();
            System.out.print("Current count " + count);
            if (count != countWorkers) {
                while (count != countWorkers) {
                    System.out.println("Waiting");
                    try {
                        iCondition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    count = counter.get();
                }
            } else {
                System.out.println("We are started!");
                iCondition.signalAll();
            }
        } finally {
            lock.unlock();
            System.out.println("Released");
        }
    }

    @Override
    public void start() {
        //working
        System.out.print("Working...");
        while (true) {
            System.out.print("..");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void close() throws Exception {
        hz.shutdown();
    }
}
