package algo;

import java.util.ArrayList;
import java.util.Arrays;

public class MinimumAbsoluteDifference {

    // Complete the minimumAbsoluteDifference function below.
    static int minimumAbsoluteDifference(int[] arr) {
        Arrays.sort(arr);
        int min = Integer.MAX_VALUE;
        for (int i = 1; i < arr.length; i++) {
            min = Math.min(
                    arr[i] - arr[i - 1],
                    min);
        }

        return min;
    }

    static int minimumAbsoluteDifference1(int[] arr) {

        int min = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (i != j) {
                    min = Math.min(
                            min,
                            Math.min(
                                    Math.abs(arr[i] - arr[j]),
                                    Math.abs(arr[j] - arr[i])
                            )
                    );
                }
            }
        }

        return min;
    }

}
