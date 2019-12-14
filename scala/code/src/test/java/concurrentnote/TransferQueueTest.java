package concurrentnote;

import org.junit.Test;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

public class TransferQueueTest {

    @Test
    public void testTransfer() throws InterruptedException {
        final TransferQueue<Integer> queue = new LinkedTransferQueue<>();
        queue.transfer(10);
    }

    @Test
    public void testTryTransfer() {
        final TransferQueue<Integer> queue = new LinkedTransferQueue<>();
        final boolean result = queue.tryTransfer(10);
        System.out.println(result);
    }

    @Test
    public void testTake() throws InterruptedException {
        final TransferQueue<Integer> queue = new LinkedTransferQueue<>();
        final Integer element = queue.take();
        System.out.println(element);

    }
}
