package javastdnote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linecorp.armeria.common.util.CompositeException;

public class SuppressedCause {

    private static final Logger logger = LoggerFactory.getLogger(SuppressedCause.class);

    public static void main(String[] args) throws Exception {
        try {
            test();
        } catch (Exception e) {
            logger.error("ex", e);
        }

        Throwable ex1 = new IllegalArgumentException();
        IllegalStateException ex2 = new IllegalStateException();
        ex1.addSuppressed(ex2);
        ex1.printStackTrace();

        new CompositeException(ex1, ex2).printStackTrace();
    }

    static class ResourceA implements AutoCloseable {
        public void read() throws Exception {
            throw new Exception("ResourceA read exception");
        }

        @Override
        public void close() throws Exception {
            throw new Exception("ResourceA close exception");
        }
    }

    static class ResourceB implements AutoCloseable {
        public void read() throws Exception {
            throw new Exception("ResourceB read exception");
        }

        @Override
        public void close() throws Exception {
            throw new Exception("ResourceB close exception");
        }
    }

    public static void test() throws Exception {
        try (ResourceA a = new ResourceA();
             //ResourceB b = new ResourceB()
        ) {
            a.read();
            //b.read();
        } catch (Exception e) {
            throw e;
        }
    }
}
