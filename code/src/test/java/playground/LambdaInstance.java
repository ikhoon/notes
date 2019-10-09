package playground;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import recursionschemenote.Json.Obj;

public class LambdaInstance {

    String foo() {
        return "abc";
    }

    String foo(Supplier<String> s) {
        System.out.println(s);
        return "abc " + s.get();
    }

    @Test
    public void sample() {
        IntStream.range(1, 10)
                 .forEach(i -> foo(this::foo));
    }

    static String bar() {
        return "abc";
    }

    @Test
    public void lambdaStatic() {
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
