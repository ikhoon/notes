package algo;

import org.junit.Test;

import static org.junit.Assert.*;

public class ContainerWithMostWaterTest {


    @Test
    public void test1() {
        int[] nums = {1,8,6,2,5,4,8,3,7};
        ContainerWithMostWater containerWithMostWater = new ContainerWithMostWater();
        int i = containerWithMostWater.maxArea(nums);
        int expected = 49;
        assertEquals(49, i);
    }
}