package algo;

import java.util.PriorityQueue;

public class LuckBalance {

    // Complete the luckBalance function below.
    static int luckBalance(int k, int[][] contests) {

        // T = 0은 다 넣는다
        // T = 1것은 k개 만 넣는다.
        // 제일 작은 값을 제거 한다.
        int total = 0;
        PriorityQueue<Integer> integers = new PriorityQueue<>();
        for (int i = 0; i < contests.length; i++) {
            int[] contest = contests[i];
            boolean isImportant = contest[1] == 1;
            int luck = contest[0];
            if(isImportant) {
                integers.add(luck);
                if (integers.size() > k) {
                    int lose = integers.poll();
                    total -= lose;
                }
            }
            else {
                total += luck;
            }
        }
        for (int luck: integers) {
           total += luck;
        }
        return total;
    }

}
