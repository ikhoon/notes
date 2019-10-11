package playground;

import java.util.function.Supplier;
import java.util.stream.IntStream;

import org.junit.Test;

public class LambdaInstance {

    String foo() {
        return "abc";
    }

    String foo(Supplier<LambdaInstance> s) {
        System.out.println(s);
        return "abc " + s.get();
    }


    static LambdaInstance instance = new LambdaInstance();
    static LambdaInstance bar() {
        return instance;
    }

    @Test
    public void lambdaStatic() {
        final Supplier<LambdaInstance> a = LambdaInstance::bar;
        System.out.println(a);
        IntStream.range(1, 10)
                 .forEach(i -> foo(LambdaInstance::bar));
    }

    String quz(int a) {
        System.out.println(this);
        return "abc";
    }

    @Test
    public void lambdaWithParam() {
        System.out.println(this);
        IntStream.range(1, 10)
                 .forEach(this::quz);
    }

    @Test
    public void testLambda() {
        Throwable cause = new Exception();
        for (int i = 0; i < 10; i++) {
            final Supplier<Throwable> a = () -> cause;
            System.out.println(a);
        }
    }
}
