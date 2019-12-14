package concurrentnote;

import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class SynchronousQueueTest {


    @Test
    public void test() throws InterruptedException {
        Executor executor = Executors.newSingleThreadExecutor();
        SynchronousQueue<String> queue = new SynchronousQueue<>();
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

    @Test
    public void testPut() throws InterruptedException {
        final SynchronousQueue<Integer> queue = new SynchronousQueue<>();
        for (int i = 0; i < 5; i++) {
            queue.put(i);
            System.out.println("put $i");
        }
    }

    @Test
    public void testTake() throws InterruptedException {
        final SynchronousQueue<Integer> queue = new SynchronousQueue<>();
        final Integer element = queue.take();
        System.out.println(element);
    }
}
