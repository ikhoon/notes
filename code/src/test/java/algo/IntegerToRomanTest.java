package algo;

import org.junit.Test;

import static org.junit.Assert.*;

public class IntegerToRomanTest {


    @Test
    public void test1() {
        int input = 58;
        String expected = "LVIII";
        IntegerToRoman integerToRoman = new IntegerToRoman();
        String s = integerToRoman.intToRoman(input);
        assertEquals(expected, s);
    }
}