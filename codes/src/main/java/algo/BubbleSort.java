package algo;

import java.util.function.UnaryOperator;

public class BubbleSort {
    // Complete the countSwaps function below.
    static void countSwaps(int[] a) {
        boolean isSorted = false;
        int lastUnsortedPosition = a.length - 1;
        int totalSwapped = 0;
        while(!isSorted) {
            isSorted = true;
            for (int i = 0; i < lastUnsortedPosition; i++) {
               if(a[i] > a[i + 1]) {
                   swap(a, i, i + 1);
                   totalSwapped += 1;
                   isSorted = false;
               }
            }
            lastUnsortedPosition -= 1;
        }

        System.out.println("Array is sorted in " + totalSwapped + " swaps.");
        System.out.println("First Element: " + a[0]);
        System.out.println("Last Element: " + a[a.length - 1]);
    }

    static void swap(int[] arr, int x, int y){
        int tmp = arr[x];
        arr[x] = arr[y];
        arr[y] = tmp;
    }
}
