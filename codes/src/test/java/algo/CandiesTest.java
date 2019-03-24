package algo;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static algo.Candies.candies;
import static algo.Candies.getCandies;
import static org.junit.Assert.*;

public class CandiesTest {

    @Test
    public void test1() {
        int n = 10;
        int[] arr = { 2, 4, 2, 6, 1, 7, 8, 9, 2, 1 };
        Integer[] empty = new Integer[0];
        int[] actual = getCandies(n, arr);
        List<Integer> expected = Arrays.asList( 1, 2, 1, 2, 1, 2, 3, 4, 2, 1);

        assertEquals(expected, actual);
    }

    @Test
    public void testWithInput() {

        Scanner scanner = TestInputScanner.getScanner("candies.txt");
        int n = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        int[] arr = new int[n];

        for (int i = 0; i < n; i++) {
            int arrItem = scanner.nextInt();
            scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");
            arr[i] = arrItem;
        }
        int[] result = getCandies(n, arr);
        int result2 = candies(n, arr);

//        for (int i = 0; i < result.size(); i++) {
//            System.out.print(" " + arr[i] + "," + result.get(i) + " ");
//            if(i % 10 == 9) {
//                System.out.println();
//            }
//        }

        assertEquals(result2, 33556);

    }

    @Test
    public void test2() {
        int[] arr = { 16387, 59801, 2225, 51489, 63693, 65074, 30389, 92493, 49135, 83523, 37766, 16728, 74433, 64881, 4280, 93171, 91649 };
        int[] candies = getCandies(arr.length, arr);
        System.out.println("candies = " + Arrays.toString(candies));

    }
}