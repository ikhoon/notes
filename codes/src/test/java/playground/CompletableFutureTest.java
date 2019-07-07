package playground;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.omg.SendingContext.RunTime;

public class CompletableFutureTest {

    @Test
    public void testExceptionAndSet() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> cf = new CompletableFuture<Integer>();
        cf.completeExceptionally(new RuntimeException(""));
        Thread.sleep(100);
        cf.complete(10);
        Integer integer = cf.get();
    }

    @Test
    public void testSetAndException() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> cf = new CompletableFuture<Integer>();
        cf.complete(10);
        cf.completeExceptionally(new RuntimeException(""));
        Integer integer = cf.get();
    }

    @Test
    public void testCancel() throws ExecutionException, InterruptedException {
    }

}
