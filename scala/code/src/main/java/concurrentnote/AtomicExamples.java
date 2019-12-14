package concurrentnote;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.Supplier;

public class AtomicExamples {
    private final AtomicReferenceFieldUpdater<AtomicExamples, Supplier> supplierUpdater = AtomicReferenceFieldUpdater
            .newUpdater(AtomicExamples.class,Supplier.class, "supplier");

    private volatile Supplier<? extends Throwable> supplier;

    public Supplier<? extends Throwable> update(Supplier<? extends Throwable> supplier) {
        supplierUpdater.compareAndSet(this, null, supplier);
        return this.supplier;
    }

}
