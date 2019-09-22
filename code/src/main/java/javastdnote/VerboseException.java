package javastdnote;

public class VerboseException {

    public static void foo() {
        throw new FooException();
    }
    public static void bar() {
        throw new FooException(false);
    }
    public static void quz() {
        bar();
    }
    public static void main(String[] args) {
       quz();
    }
}
class FooException extends RuntimeException {
    FooException() {}
    static final FooException instance = new FooException(false);
    static final FooException get() {
       return instance;
    }

    FooException(boolean dummy) {
        super(null, null, false, false);
    }
}
