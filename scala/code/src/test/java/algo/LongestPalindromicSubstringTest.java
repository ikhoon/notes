package algo;

import org.junit.Test;

import static org.junit.Assert.*;

public class LongestPalindromicSubstringTest {


    @Test
    public void test1() {
        LongestPalindromicSubstring longestPalindromicSubstring = new LongestPalindromicSubstring();
        longestPalindromicSubstring.longestPalindrome("babad");
    }
}