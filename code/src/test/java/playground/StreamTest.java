package playground;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.linecorp.armeria.internal.shaded.guava.collect.ImmutableList;

public class StreamTest {
    @Test
    public void test() {
        List<Integer> xs = ImmutableList.of(1, 2, 3, 4, 5);
        xs.stream()
          .map(x -> {
              System.out.println("hello: " + x);
              return x;
          }).collect(Collectors.toList());
    }

    @Test
    public void repeatTest() {
        List<Integer> xs = ImmutableList.of(1, 2, 3, 4, 5);
        final Stream<Integer> stream = xs.stream();
        stream.forEach(System.out::println);
        stream.forEach(System.out::println);
        stream.forEach(System.out::println);
        stream.forEach(System.out::println);

    }

}
