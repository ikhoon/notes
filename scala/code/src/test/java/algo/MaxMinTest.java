package algo;

import org.junit.Test;

import static org.junit.Assert.*;

public class MaxMinTest {

    @Test
    public void test1() {
        int[] arr = {10, 100, 300, 200, 1000, 20, 30};
        int k = 3;
        int unfairness = MaxMin.maxMin(k, arr);
        assertEquals(20, unfairness);

    }

    @Test
    public void test2() {
        int[] arr = { 1, 2, 3, 4, 10, 20, 30, 40, 100, 200 };
        int k = 4;
        int unfairness = MaxMin.maxMin(k, arr);
        assertEquals(3, unfairness);
    }

    @Test
    public void test3() {
        int[] arr = {1, 2, 1, 2, 1 };
        int k = 2;
        int unfairness = MaxMin.maxMin(k, arr);
        assertEquals(0, unfairness);
    }

    @Test
    public void test4() {

        int[] arr = { 4504, 1520, 5857, 4094, 4157, 3902, 822, 6643, 2422, 7288, 8245, 9948, 2822,
        1784, 7802, 3142, 9739, 5629, 5413, 7232 };
        int k = 5;
        int unfairness = MaxMin.maxMin(k, arr);
        assertEquals(1335, unfairness);
    }

}