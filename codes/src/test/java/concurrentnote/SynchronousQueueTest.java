package concurrentnote;

import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import scalaz.Alpha;

import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class SynchronousQueueTest {

    Executor executor = Executors.newSingleThreadExecutor();
    SynchronousQueue<String> queue = new SynchronousQueue<>();

    @Test
    public void test() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        executor.execute(() -> {
            while (true) {
                final int count = counter.incrementAndGet();
                System.out.println("hello: " + count);
                try {
                    queue.put("hello: " + count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread.sleep(10000);
    }
}
