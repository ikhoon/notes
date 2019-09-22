package algo;

import org.junit.Test;

import static org.junit.Assert.*;

public class AtoITest {


    @Test
    public void test1() {
        String input = "   -42";
        int output = -42;
        AtoI aToI = new AtoI();
        int actual = aToI.myAtoi(input);
        assertEquals(output, actual);
    }

    @Test
    public void test2() {
        String input = "words and 987";
        int output = 0;
        AtoI aToI = new AtoI();
        int actual = aToI.myAtoi(input);
        assertEquals(output, actual);
    }

    @Test
    public void test3() {
        String input = "+1";
        int output = 1;
        AtoI aToI = new AtoI();
        int actual = aToI.myAtoi(input);
        assertEquals(output, actual);
    }
}