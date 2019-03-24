package algo;

import org.junit.Test;

import static org.junit.Assert.*;

public class RomanToIntegerTest {

    @Test
    public void test1() {
        String roman = "LVIII";
        int expected = 58;
        RomanToInteger romanToInteger = new RomanToInteger();
        int actual = romanToInteger.romanToInt(roman);
        assertEquals(expected, actual);
    }

    @Test
    public void test2() {
        String roman = "MCMXCIV";
        int expected = 1994;
        RomanToInteger romanToInteger = new RomanToInteger();
        int actual = romanToInteger.romanToInt(roman);
        assertEquals(expected, actual);
    }
}