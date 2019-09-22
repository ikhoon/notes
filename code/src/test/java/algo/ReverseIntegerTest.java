package algo;

import org.junit.Test;

import static org.junit.Assert.*;

public class ReverseIntegerTest {


    @Test
    public void test1() {
        int a = 120;
        ReverseInteger reverseInteger = new ReverseInteger();
        int actual = reverseInteger.reverse(120);
        int expected = 21;
        assertEquals(21, actual);
    }

    @Test
    public void test() {
        int a = -2147483648;

        ReverseInteger reverseInteger = new ReverseInteger();
        int actual = reverseInteger.reverse(a);
        int expected = 21;
        assertEquals(21, actual);
    }
}