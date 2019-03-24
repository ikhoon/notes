package algo;

import org.junit.Test;

import static org.junit.Assert.*;

public class LongestCommonPrefixTest {


    @Test
    public void test1() {

        String[] strs = {
                "flower","flow","flight"
        };
        LongestCommonPrefix longestCommonPrefix = new LongestCommonPrefix();

        String s = longestCommonPrefix.longestCommonPrefix(strs);
        String expected = "fl";
        assertEquals(expected, s);
    }
}