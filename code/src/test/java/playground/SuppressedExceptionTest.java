package playground;

import org.junit.Test;

public class SuppressedExceptionTest {

    class Foo implements AutoCloseable {

        @Override
        public void close() throws Exception {
            System.out.println("foo close");
            throw new RuntimeException("hello");
        }
    }
    class Bar implements AutoCloseable {

        @Override
        public void close() throws Exception {
            System.out.println("bar close");
            throw new SuppressedException();
        }
    }
    class SuppressedException extends RuntimeException {
        public SuppressedException() {
            super(null, null, false, false);
        }
    }
    @Test
    public void testSuppressed() {
        try(Foo fo = new Foo()) {

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getSuppressed().length);
        }
    }

    @Test
    public void testSuppressed2() {
        try(Bar bar = new Bar()) {

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getSuppressed().length);
        }

    }
}
