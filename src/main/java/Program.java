public class Program {

    private static final int LIMIT = 2;

    /**
     * Code sample from Mastering Hazelcast (3.5 ICondition)
     */
    public static void main (String[] args){

        try(Worker worker = new HazelcastWorkerImpl()){
            worker.waitTill(LIMIT);
            worker.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
