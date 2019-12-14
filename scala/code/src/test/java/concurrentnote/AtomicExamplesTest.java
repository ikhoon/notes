package concurrentnote;

import static org.junit.Assert.*;

import java.util.function.Supplier;

import org.junit.Test;

public class AtomicExamplesTest {

    @Test
    public void test() {
        final AtomicExamples examples = new AtomicExamples();
        final Supplier<? extends Throwable> supplier = RuntimeException::new;
        final Supplier<? extends Throwable> update = examples.update(supplier);
        assertSame(supplier, update);
    }
}