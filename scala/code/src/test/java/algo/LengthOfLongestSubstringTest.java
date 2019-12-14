package algo;

import org.junit.Test;

import static org.junit.Assert.*;

public class LengthOfLongestSubstringTest {


    @Test
    public void test1() {
        LengthOfLongestSubstring lengthOfLongestSubstring = new LengthOfLongestSubstring();
        String s = "abcabcbb";
        int noRepeat = lengthOfLongestSubstring.lengthOfLongestSubstring(s);
        assertEquals(3, noRepeat);
    }


    @Test
    public void test2() {

        LengthOfLongestSubstring lengthOfLongestSubstring = new LengthOfLongestSubstring();
        String s = "tmmzuxt";
        int noRepeat = lengthOfLongestSubstring.lengthOfLongestSubstring(s);
        assertEquals(5, noRepeat);
    }
}