package playground;

import java.util.List;
import java.util.stream.Collectors;

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
}
