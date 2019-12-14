package algo;

import org.junit.Test;

import static org.junit.Assert.*;

public class HourClassSumTest {

    @Test
    public void test1() {
        int[][] arr = {
                {1, 1, 1, 0, 0, 0},
                {0, 1, 0, 0, 0, 0},
                {1, 1, 1, 0, 0, 0},
                {0, 0, 2, 4, 4, 0},
                {0, 0, 0, 2, 0, 0},
                {0, 0, 1, 2, 4, 0}
        };

        int result = HourClassSum.hourglassSum(arr);
        assertEquals(19, result);
    }
}