public interface Worker extends AutoCloseable {

    void waitTill(int countWorkers);

    void start();
}
