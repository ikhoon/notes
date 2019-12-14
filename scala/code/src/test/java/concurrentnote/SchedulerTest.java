package concurrentnote;

import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

public class SchedulerTest {
    @Test
    public void test() throws ExecutionException, InterruptedException {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        Future<Integer> submit = exec.submit(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                return 100;
            }
        });
        while (!submit.isDone()) {}
        System.out.println(submit.get());
    }
}
