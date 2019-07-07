package javastdnote;

final class FallthroughException extends RuntimeException {

    private static final long serialVersionUID = 3856883467407862925L;

    private static final FallthroughException INSTANCE = new FallthroughException();

    /**
     * Returns a singleton {@link FallthroughException}.
     */
    public static FallthroughException get() {
        return INSTANCE;
    }

    private FallthroughException() {
        super((Throwable) null);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}


public class FillInStackTrace {

    private static void f1() throws Exception {
        System.out.println("call f1");
        throw FallthroughException.get();
    }
    private static void f2() throws Throwable {
        try {
            f1();
        } catch(Exception e) {
            System.out.println("call f2");
            throw e;
        }
    }

    public static void main(String[] args) {
        try {
            f2();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
