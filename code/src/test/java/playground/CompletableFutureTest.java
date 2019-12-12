package playground;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.linecorp.armeria.internal.shaded.futures.CompletableFutures;

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
        CompletableFuture<Integer> cf2 = CompletableFutures.exceptionallyCompletedFuture(
                new Exception("Bang!"));

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
        CompletableFuture<Integer> cf2 = CompletableFutures.exceptionallyCompletedFuture(
                new Exception("Bang2!"));
        CompletableFuture<Integer> cf3 = CompletableFutures.exceptionallyCompletedFuture(
                new Exception("Bang3!"));
        Void join = CompletableFuture.allOf(cf1, cf2, cf3).join();
        System.out.println(join);
    }

    @Test
    public void testAllOfHandleException() {
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("hello cf1");
            return 10;
        });
        CompletableFuture<Integer> cf2 = CompletableFutures.exceptionallyCompletedFuture(
                new Exception("Bang2!"));

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

    @Test
    public void testHandleWorkerThread() {
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("hello cf1: " + Thread.currentThread().getName());
            return 10 / 0;
        });

        final CompletableFuture<Object> cf2 = cf1.handle((result, cause) -> {
            System.out.println("handle cf1: " + Thread.currentThread().getName());
            return null;
        });
        await().untilAsserted(() -> assertThat(cf2.isDone()).isTrue());
        System.out.println("finish?");

    }

    @Test
    public void testHandleMainThread() {
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("hello cf1: " + Thread.currentThread().getName());
            return 10 / 0;
        });

        await().untilAsserted(() -> assertThat(cf1.isCompletedExceptionally()).isTrue());
        cf1.handle((result, cause) -> {
            System.out.println("handle cf1: " + Thread.currentThread().getName());
            return null;
        });
        System.out.println("finish?");
    }

    @Test
    public void testCompletableFuturesSuccessfulAsList() {
        final List<CompletableFuture<Void>> futures = new ArrayList<>();
        final CompletableFuture<Object> handled = CompletableFutures
                .successfulAsList(futures, unused -> null)
                .handle((unused1, unused2) -> {
                    System.out.println("call handle");
                    return null;
                });

        handled.join();
    }

}
