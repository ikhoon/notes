package algo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Candies {
    // Complete the candies function below.
    static int candies(int n, int[] arr) {
        int[] all = getCandies(n, arr);
        int sum = 0;
        for (int i : all) {
           sum += i;
        }
        return sum;
    }

    static int[] getCandies(int n, int[] arr) {
        int[] candieList = new int[n];
        // init
        candieList[0] = 1;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > arr[i - 1]) {
                candieList[i] = candieList[i - 1] + 1;
            } else {
                candieList[i] = 1;
            }
        }

        for (int i = candieList.length - 2; i >= 0; i--) {
            // 같으면 값을 증가시킨다.
            if (candieList[i] <= candieList[i + 1]) {
                // 원래 값을 확인해보자
                // arr[i] == arr[i + 1]
                // 그냥 arr[i]을 증가해주면 됨
                //
                if (arr[i] > arr[i + 1]) {
                    candieList[i] = candieList[i + 1] + 1;
                }
            }
        }

        return candieList;
    }
}
