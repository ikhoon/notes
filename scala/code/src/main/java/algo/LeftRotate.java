package algo;

import java.util.Arrays;

public class LeftRotate {
    // Complete the rotateLeft function below.
    static int[] rotateLeft(int[] a, int d) {

        int[] shifted = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            // 5개 크기 1 => left 4 => 2 => 1 - 4 + 5 = 2
           shifted[(i - d + a.length) % a.length] = a[i];
        }
        return shifted;
    }

}
