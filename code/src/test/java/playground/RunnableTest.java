package playground;

import org.junit.Test;

public class RunnableTest {
    class FooTask implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("hello " + Thread.currentThread());
        }
    }

    @Test
    public void test() {
        new FooTask().run();
    }

}
