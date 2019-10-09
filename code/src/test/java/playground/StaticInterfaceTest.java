package playground;

import static org.assertj.core.api.Assertions.assertThat;
import static playground.StaticInterface.foo;

import org.junit.Test;

public class StaticInterfaceTest {

    @Test
    public static void test() {
        assertThat(foo()).isEqualTo("foobar");
    }

}