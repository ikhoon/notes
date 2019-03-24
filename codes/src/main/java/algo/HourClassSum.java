package algo;

public class HourClassSum {

    // Complete the hourglassSum function below.
    static int hourglassSum(int[][] arr) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length - 2; i++) {
            for (int j = 0; j < arr[i].length - 2; j++) {
                max = Math.max(hourclassSum0(arr, i, j), max);
            }
        }

        return max;
    }

    static int hourclassSum0(int[][] arr, int x, int y) {
        return arr[x][y] + arr[x][y + 1] + arr[x][y + 2] +
                arr[x + 1][y + 1] +
                arr[x + 2][y] + arr[x + 2][y + 1] + arr[x + 2][y + 2];
    }
}
