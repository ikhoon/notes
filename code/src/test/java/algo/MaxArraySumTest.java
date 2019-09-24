package algo;

import org.junit.Test;

import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

import static algo.MaxArraySum.maxSubsetSum;
import static org.junit.Assert.*;

public class MaxArraySumTest {

    @Test
    public void testMaxArraySum() {

        int[] xs = { -2, 1, 3, -4, 5 };
        assert maxSubsetSum(xs) == 8;
    }

    @Test
    public void testMaxArraySum2() {
        int[] xs = { 3, 7, 4, 6, 5 };
        assert maxSubsetSum(xs) == 13;

        int[] xs2 = {2, 1, 5, 8, 4 };
        assert maxSubsetSum(xs2) == 11;

        int[] xs3 = { 3, 5, -7, 8, 10 };
        assert maxSubsetSum(xs3) == 15;

    }


    @Test
    public void testMaxArraySum3() throws IOException {
       System.out.println("maxSubsetSum(getInput()) = " + maxSubsetSum(getInput()));
    }

    @Test
    public void testGetInput() throws IOException {

        System.out.println(getInput().length);

    }
    @Test
    public void testListEquals() {
        List<Integer> xs = Arrays.asList(1, 2);
        System.out.println(xs);
        List<Integer> ys = Arrays.asList(1, 2);
        System.out.println(ys);
        assert xs.equals(ys);

        Map<List<Integer>, Integer> map = new HashMap<>();
        map.put(xs, 10);
        assert map.get(ys) == 10;
    }

    public int[] getInput() throws IOException {
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/maxArraySum.txt");
        final Scanner scanner = new Scanner(resourceAsStream);

        int n = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        int[] arr = new int[n];

        String[] arrItems = scanner.nextLine().split(" ");
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int i = 0; i < n; i++) {
            int arrItem = Integer.parseInt(arrItems[i]);
            arr[i] = arrItem;
        }
        scanner.close();

        return arr;

    }
}