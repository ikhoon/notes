package concurrentnote;

import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class BlockingQueueTest {
    Executor executor = Executors.newSingleThreadExecutor();

    @Test
    public void test() {
        BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<>();
    }
}
