package org.frameworks.hazelcast;

import org.frameworks.common.Worker;

public class Program {

    private static final int LIMIT = 10;

    /**
     * Code sample from Mastering Hazelcast (3.5 ICondition)
     */
    public static void main(String[] args) {
        try (Worker worker = new HazelcastWorkerImpl()) {
            worker.waitTill(LIMIT);
            worker.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
