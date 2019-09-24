package algo;

public class MininumBribes {

    // Complete the minimumBribes function below.
    static void minimumBribes(int[] q) {

        int i = minimumBribes0(q);
        if(i == -1) {
            System.out.println("Too chaotic");
        }
        else {
            System.out.println(i);
        }


    }

    static int minimumBribes0(int[] q) {
        int total = 0;
        for (int i = q.length - 1; i >= 0; i--) {
            int current = q[i];
            if(current > i + 3) {
                return -1;
            }
            for (int j = Math.max(0, current - 2); j < i; j++) {
                if(q[j] > current) {
                    total += 1;
                }
            }

        }
        return total;
    }
}
