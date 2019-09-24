package algo;

import java.util.HashMap;
import java.util.Map;

public class TwoSum {

    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], i);
        }

        for (int i = 0; i < nums.length; i++) {
            Integer value = map.get(target - nums[i]);
            if(value != null && value != i) {
               return new int[]{i, value};
            }
        }
        return new int[] {};
    }

    public int[] twoSum2(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            int first = nums[i];
            for (int j = 0; j < nums.length; j++) {
                if(i != j) {
                    int second = nums[j];
                    if(first + second == target) {
                        return new int[]{i, j};
                    }
                }
            }
        }
        return new int[]{};
    }
}
