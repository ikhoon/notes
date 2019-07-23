package playground;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

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
    public void testAllOfException() {
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("hello cf1");
            return 10;
        });
        CompletableFuture<Integer> cf2 = CompletableFuture.failedFuture(new Exception("Bang!"));

        CompletableFuture<Integer> cf3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("hello cf3");
            return 20;
        });
        Void join = CompletableFuture.allOf(cf1, cf2, cf3).join();
    }

    @Test
    public void testAllOf() {
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("hello cf1");
            return 10;
        });
        CompletableFuture<Integer> cf2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("hello cf2");
            return 15;
        });

        CompletableFuture<Integer> cf3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("hello cf3");
            return 20;
        });
        Void join = CompletableFuture.allOf(cf1, cf2, cf3).join();
        System.out.println(join);
    }

    @Test
    public void testAllOfTwoException() {
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("hello cf1");
            return 10;
        });
        CompletableFuture<Integer> cf2 = CompletableFuture.failedFuture(new Exception("Bang2!"));
        CompletableFuture<Integer> cf3 = CompletableFuture.failedFuture(new Exception("Bang3!"));
        Void join = CompletableFuture.allOf(cf1, cf2, cf3).join();
        System.out.println(join);
    }

    @Test
    public void testAllOfHandleException() {
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("hello cf1");
            return 10;
        });
        CompletableFuture<Integer> cf2 = CompletableFuture.failedFuture(new Exception("Bang2!"));

        CompletableFuture<Integer> cf3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("hello cf3");
            return 20;
        });
        CompletableFuture<Integer> cf2handled = cf2.handle((result, cause) -> {
            if (cause != null) {
                cause.printStackTrace();
                return null;
            }
            return result;
        });
        Void join = CompletableFuture.allOf(cf1, cf2handled, cf3).join();
        System.out.println(join);

    }

    @Test
    public void testWhenComplete() {
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("hello cf1");
            return 10;
        });
    }

}
