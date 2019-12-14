package algo;



import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class LeftRotateTest {


    @Test
    public void testLeftRotate() {
        int[] arr = {1, 2, 3, 4, 5};
        int n = 4;

        int[] ints = LeftRotate.rotateLeft(arr, n);
        System.out.println("ints = " + Arrays.toString(ints));
        int[] expected = {5, 1, 2, 3, 4};
        Assert.assertArrayEquals(expected, ints);
    }



}