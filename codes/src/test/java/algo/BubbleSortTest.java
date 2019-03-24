package algo;

import org.junit.Test;

import static org.junit.Assert.*;

public class BubbleSortTest {

    @Test
    public void test1() {

        int[] array = { 3, 2, 1 };
        BubbleSort.countSwaps(array);
    }
}