package org.ngsandbox.common;

public interface Worker extends AutoCloseable {

    void waitTill(int countWorkers) throws Exception;

    default void start(){
        //working
        int counts = 100;
        System.out.print("Working...");
        while (counts > 0) {
            System.out.print("..");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            counts--;
        }
    }
}
