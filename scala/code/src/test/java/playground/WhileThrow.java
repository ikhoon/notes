package playground;

import org.junit.Test;

public class WhileThrow {
    @Test
    public void test() {
        while (true) {
            throw new RuntimeException("Oops!");
        }
    }
}
