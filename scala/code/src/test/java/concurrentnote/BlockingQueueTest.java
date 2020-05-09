package concurrentnote;

import org.junit.Test;

import java.util.concurrent.*;

import io.reactivex.rxjava3.exceptions.Exceptions;

public class BlockingQueueTest {
    Executor executor = Executors.newSingleThreadExecutor();

    @Test
    public void testPut() throws InterruptedException {
        final BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<>();
        final CountDownLatch latch = new CountDownLatch(1);
        executor.execute(() -> {
            try {
                Thread.sleep(10);
                blockingQueue.put("hello");
                System.out.println("put hello");
                blockingQueue.put("hello");
                System.out.println("put hello");
                latch.countDown();
            } catch (InterruptedException e) {
                Exceptions.propagate(e);
            }
        });
        latch.await();
    }

    @Test
    public void testGet() throws InterruptedException {
        final BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<>();
        // pending infinity
        final String element = blockingQueue.take();
        System.out.println(element);
    }
}
