package algo;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

public class MaxMin {
    // Complete the maxMin function below.
    static int maxMin(int k, int[] arr) {
        Arrays.sort(arr);

        // 정렬하고
        // 스캔을 하면서 min값과 max가 최소가 되도록 하자.
        int minPos = 0;
        int maxPos = k - 1;
        for (int i = k; i < arr.length; i++) {
            // 현재 배열이 들어갔을때의 거리
            int newDistance = arr[i] - arr[i - k + 1];
            int prevDistance = arr[maxPos] - arr[minPos];
            if(newDistance < prevDistance) {
               minPos = i - k + 1;
               maxPos = i;
            }
        }
        return arr[maxPos] - arr[minPos];
    }
}
