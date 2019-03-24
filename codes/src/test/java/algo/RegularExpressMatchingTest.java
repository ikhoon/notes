package algo;

import org.junit.Test;

import static org.junit.Assert.*;

public class RegularExpressMatchingTest {


    @Test
    public void test1() {
        String a = "aa";
        String b = "a";
        RegularExpressMatching regularExpressMatching = new RegularExpressMatching();
        boolean match = regularExpressMatching.isMatch(a, b);
        assertEquals(false, match);
    }

}