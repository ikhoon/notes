package javastdnote;

import org.junit.Test;

public class NullInstanceOf {
    @Test
    public void test() {
        final boolean isString = null instanceof String;
        System.out.println("isString = " + isString);
    }
}
