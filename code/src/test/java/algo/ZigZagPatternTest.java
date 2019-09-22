package algo;

import org.junit.Test;

import static org.junit.Assert.*;

public class ZigZagPatternTest {


    ZigZagPattern zigZagPattern = new ZigZagPattern();
    @Test
    public void test1() {
        String input = "PAYPALISHIRING";
        int numRows = 3;
        String expected = "PAHNAPLSIIGYIR";
        String convert = zigZagPattern.convert(input, numRows);
        assertEquals(expected, convert);
    }

    @Test
    public void test2() {
        String input = "AB";
        int numRows = 1;
        String expected = "AB";
        String convert = zigZagPattern.convert(input, numRows);
        assertEquals(expected, convert);
    }
}