package algo;

import org.junit.Test;

import static org.junit.Assert.*;

public class MinimumAbsoluteDifferenceTest {

    @Test
    public void test1() {
        int[] xs = {-59, -36, -13, 1, -53, -92, -2, -96, -54, 75};
        int min = MinimumAbsoluteDifference.minimumAbsoluteDifference(xs);
        assertEquals(1, min);
    }
}