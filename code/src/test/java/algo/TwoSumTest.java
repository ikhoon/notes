package algo;

import org.junit.Test;

import static org.junit.Assert.*;

public class TwoSumTest {


    @Test
    public void test1() {
        int [] nums = {2, 7, 11, 15};
        int target = 9;
        TwoSum twoSum = new TwoSum();
        int[] ints = twoSum.twoSum(nums, target);
        assertArrayEquals(new int[]{0, 1}, ints);
    }

    @Test
    public void test2() {
        int [] nums = {3, 2, 4};
        int target = 6;
        TwoSum twoSum = new TwoSum();
        int[] ints = twoSum.twoSum(nums, target);
        assertArrayEquals(new int[]{1, 2}, ints);

    }
}