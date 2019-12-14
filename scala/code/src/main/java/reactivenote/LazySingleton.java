package reactivenote;

import java.util.concurrent.atomic.AtomicBoolean;

public class LazySingleton {
    AtomicBoolean isInit = new AtomicBoolean();
    volatile LazySingleton instance = null;

    public LazySingleton get() {
        if(instance != null) {
            return instance;
        }
        if(isInit.compareAndSet(false, true)) {
            instance = new LazySingleton();
        }
        while (instance == null) {
            Thread.onSpinWait();
        }
        return instance;
    }
}
