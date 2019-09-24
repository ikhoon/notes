package algo;

import org.junit.Test;

import static org.junit.Assert.*;

public class LuckBalanceTest {

    @Test
    public void test1() {
        int[][] contents = {
                {5, 1},
                {2, 1},
                {1, 1},
                {8, 1},
                {10, 0},
                {5, 0}
        };
        int k = 3;
        assertEquals(29, LuckBalance.luckBalance(k, contents));
    }

}