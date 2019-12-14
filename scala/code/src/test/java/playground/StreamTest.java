package playground;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

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

    @Test
    public void streamEmpty() {
        final Stream<String> concat = Stream.concat(Stream.empty(), Stream.of("a", "b"));
        final List<String> list = concat.collect(Collectors.toList());
        System.out.println(list);
        final List<String> list1 = Arrays.asList(null, "10")
                                         .stream().collect(Collectors.toList());
        System.out.println(list1);

    }
}
