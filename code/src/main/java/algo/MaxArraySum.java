package algo;


import java.util.*;

public class MaxArraySum {

    // Complete the maxSubsetSum function below.
    static int maxSubsetSum(int[] arr) {
        return maxSubsetSum0(arr);
    }

    static int maxSubsetSum0(int[] xs) {
        int include = xs[0];
        int exclude = 0;
        int exclude_new;
        for (int i = 1; i < xs.length; i++) {
            exclude_new = Math.max(include, exclude);
            include = exclude + xs[i];
            exclude = exclude_new;
        }
        return Math.max(include, exclude);
    }

}


